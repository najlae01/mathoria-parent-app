package com.rokku.mathoria.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.rokku.mathoria.model.ParentUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.security.MessageDigest


private val Application.dataStore by preferencesDataStore("auth_preferences")

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Firebase.database.reference

    private val _loginState = mutableStateOf(false)
    val loginState: State<Boolean> = _loginState

    private val _mustChangePassword = mutableStateOf(false)
    val mustChangePassword: State<Boolean> = _mustChangePassword

    private val _loginError = mutableStateOf("")
    val loginError: State<String> = _loginError

    private val _currentUsername = MutableStateFlow("")
    val currentUsername: StateFlow<String> = _currentUsername.asStateFlow()

    private val dataStore = application.dataStore
    private val LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in")
    private val USERNAME_KEY = stringPreferencesKey("username")
    private val FIRSTNAME_KEY = stringPreferencesKey("firstname")
    private val LASTNAME_KEY = stringPreferencesKey("lastname")

    private val _dataStoreLoaded = mutableStateOf(false)
    val dataStoreLoaded: State<Boolean> = _dataStoreLoaded

    private val _currentFirstName = MutableStateFlow("")
    val currentFirstName: StateFlow<String> = _currentFirstName.asStateFlow()

    private val _currentLastName = MutableStateFlow("")
    val currentLastName: StateFlow<String> = _currentLastName.asStateFlow()

    init {
        checkIfLoggedIn()
    }

    private fun checkIfLoggedIn() {
        viewModelScope.launch {
            val isLoggedIn = dataStore.data.first()[LOGGED_IN_KEY] ?: false
            val savedUsername = dataStore.data.first()[USERNAME_KEY] ?: ""
            val savedFirstname = dataStore.data.first()[FIRSTNAME_KEY] ?: ""
            val savedLastname = dataStore.data.first()[LASTNAME_KEY] ?: ""

            if (isLoggedIn && savedUsername.isNotEmpty()) {
                _currentUsername.value = savedUsername
                _currentFirstName.value = savedFirstname
                _currentLastName.value = savedLastname
                _loginState.value = true
            }
            _dataStoreLoaded.value = true
        }
    }

    // Hash password using SHA-256 (same as crypto-js)
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(password.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    fun login(username: String, rawPassword: String, onSuccess: () -> Unit, onRequirePasswordChange: () -> Unit) {
        _loginError.value = ""
        _mustChangePassword.value = false

        db.child("users")
            .orderByChild("username")
            .equalTo(username)
            .get()
            .addOnSuccessListener { snapshot ->
                val userSnap = snapshot.children.firstOrNull()
                if (userSnap != null) {
                    val user = userSnap.getValue(ParentUser::class.java)
                    val inputHash = hashPassword(rawPassword)

                    if (user != null && user.password == inputHash) {
                        if (user.role != "Parent") {
                            _loginError.value = "Only Parent accounts are allowed to log in."
                            _loginState.value = false
                            return@addOnSuccessListener
                        }
                        _currentUsername.value = user.username
                        _currentFirstName.value = user.firstName
                        _currentLastName.value = user.lastName
                        if (user.mustChangePassword) {
                            _mustChangePassword.value = true
                            onRequirePasswordChange()
                        } else {
                            _mustChangePassword.value = false
                            _loginState.value = true
                            saveLoginState(true, username, user.firstName, user.lastName)
                            onSuccess()
                        }
                    } else {
                        _loginError.value = "Invalid Username or Password."
                        _loginState.value = false
                    }
                } else {
                    _loginError.value = "Invalid Username or Password."
                    _loginState.value = false
                }
            }
            .addOnFailureListener {
                _loginError.value = "Login failed. Please try again."
                _loginState.value = false
            }
    }

    private fun saveLoginState(isLoggedIn: Boolean, username: String,
                               firstname: String, lastname: String) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[LOGGED_IN_KEY] = isLoggedIn
                preferences[USERNAME_KEY] = username
                preferences[FIRSTNAME_KEY] = firstname
                preferences[LASTNAME_KEY] = lastname
            }
        }
    }

    fun logout() {
        _loginState.value = false
        _mustChangePassword.value = false
        _loginError.value = ""
    }

    fun setLoggedIn() {
        _loginState.value = true
        _mustChangePassword.value = false
        _loginError.value = ""
    }

}