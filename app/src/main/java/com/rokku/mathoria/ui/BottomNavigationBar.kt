package com.rokku.mathoria.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Profile,
        NavigationItem.Home,
        NavigationItem.Notifications
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = false, // We will make this active later
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }
    }
}

sealed class NavigationItem(val route: String, val icon: ImageVector, val title: String) {
    object Profile : NavigationItem("profile", Icons.Default.Person, "Profile")
    object Home : NavigationItem("home", Icons.Default.Home, "Home")
    object Notifications : NavigationItem("notifications", Icons.Default.Notifications, "Notifications")
}
