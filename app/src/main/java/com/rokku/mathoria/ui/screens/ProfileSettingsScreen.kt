package com.rokku.mathoria.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rokku.mathoria.viewmodel.AuthViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(navController: NavController,
                          authViewModel: AuthViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.titleLarge)

            SettingsItem("Edit Profile") {
                // Logic for Editing Profile (e.g., Change Username)
            }

            SettingsItem("Change Language") {
                // Logic for Changing Language
            }

            SettingsItem("Notification Preferences") {
                // Logic for Managing Notifications
            }

            SettingsItem("Theme (Light/Dark)") {
                // Logic for Changing Theme
            }

            SettingsItem("Change Password") {
                // Logic for Changing Password (show dialog)
            }

            SettingsItem("Log Out") {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo("profile_settings") { inclusive = true }
                }
            }
        }
    }
}

@Composable
fun SettingsItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.bodyLarge)
    }
}
