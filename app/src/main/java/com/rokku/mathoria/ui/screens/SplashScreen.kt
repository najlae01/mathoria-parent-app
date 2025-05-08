package com.rokku.mathoria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rokku.mathoria.viewmodel.AuthViewModel

@Composable
fun SplashScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()

    val isLoggedIn by authViewModel.loginState
    val mustChangePassword by authViewModel.mustChangePassword

    val dataStoreLoaded by authViewModel.dataStoreLoaded

    LaunchedEffect(dataStoreLoaded) {
        if (dataStoreLoaded) {
            println("🔍 Splash Debug: isLoggedIn: $isLoggedIn, mustChangePassword: $mustChangePassword")
            when {
                mustChangePassword -> {
                    navController.navigate("change_password/${authViewModel.currentUsername}") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                isLoggedIn -> {
                    navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
                else -> {
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Mathoria", style = MaterialTheme.typography.titleLarge)
            CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Loading...", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
