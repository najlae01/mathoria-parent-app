package com.rokku.mathoria

import android.app.Application
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MathoriaApplication : Application() {
    companion object {
        private val _isFirebaseReady = MutableStateFlow(false)
        val isFirebaseReady = _isFirebaseReady.asStateFlow()
    }

    override fun onCreate() {
        super.onCreate()
        initializeFirebase()
    }

    private fun initializeFirebase() {
        CoroutineScope(Dispatchers.Default).launch {
            FirebaseApp.initializeApp(this@MathoriaApplication)
            delay(1000) // Give Firebase some time to initialize (safe delay)
            _isFirebaseReady.value = FirebaseApp.getApps(this@MathoriaApplication).isNotEmpty()
        }
    }
}