package com.rokku.mathoria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rokku.mathoria.viewmodel.AuthViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.security.MessageDigest

@Composable
fun ChangePasswordScreen(navController: NavController, username: String) {
    val viewModel: AuthViewModel = viewModel()
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Change Password", style = androidx.compose.material3.MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isPasswordVisible) "Hide Password" else "Show Password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                    Icon(
                        imageVector = if (isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (isConfirmPasswordVisible) "Hide Password" else "Show Password"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message, color = androidx.compose.material3.MaterialTheme.colorScheme.error)

        Button(
            onClick = {
                if (newPassword.length < 8) {
                    message = "Password must be at least 8 characters."
                } else if (newPassword != confirmPassword) {
                    message = "Passwords do not match."
                } else {
                    changePassword(viewModel, username, newPassword) {
                        viewModel.setLoggedIn() // Mark user as logged in
                        navController.navigate("home") {
                            popUpTo("change_password/$username") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Update Password")
        }
    }
}

// Securely Hash Passwords (SHA-256)
private fun hashPassword(password: String): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val hashBytes = digest.digest(password.toByteArray())
    return hashBytes.joinToString("") { "%02x".format(it) }
}

// Method to Change Password in Firebase
private fun changePassword(viewModel: AuthViewModel, username: String, newPassword: String, onSuccess: () -> Unit) {
    val db = Firebase.database.reference
    db.child("users")
        .orderByChild("username")
        .equalTo(username)
        .get()
        .addOnSuccessListener { snapshot ->
            val userSnap = snapshot.children.firstOrNull()
            if (userSnap != null) {
                val userId = userSnap.key
                val hashedPassword = hashPassword(newPassword)

                db.child("users").child(userId!!)
                    .updateChildren(
                        mapOf(
                            "password" to hashedPassword,
                            "mustChangePassword" to false
                        )
                    ).addOnSuccessListener {
                        viewModel.setLoggedIn()
                        onSuccess()
                    }
            }
        }
}
