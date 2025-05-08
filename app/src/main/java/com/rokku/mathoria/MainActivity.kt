package com.rokku.mathoria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rokku.mathoria.ui.screens.ChangePasswordScreen
import com.rokku.mathoria.ui.screens.HomeScreen
import com.rokku.mathoria.ui.screens.LoginScreen
import com.rokku.mathoria.ui.screens.NotificationsScreen
import com.rokku.mathoria.ui.screens.ProfileScreen
import com.rokku.mathoria.ui.screens.ProfileSettingsScreen
import com.rokku.mathoria.ui.screens.SplashScreen
import com.rokku.mathoria.ui.screens.StudentProfileScreen
import com.rokku.mathoria.ui.theme.MathoriaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MathoriaTheme {
                MathoriaParentApp()
            }
        }
    }
}

@Composable
fun MathoriaParentApp() {
    val navController = rememberNavController()
    val isFirebaseReady by MathoriaApplication.isFirebaseReady.collectAsState()

    Surface(color = MaterialTheme.colorScheme.background) {
        if (isFirebaseReady) {
            ParentAppNavHost(navController)
        } else {
            FirebaseLoadingSplash()
        }
    }
}

@Composable
fun FirebaseLoadingSplash() {
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


@Composable
fun ParentAppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("student_profile/{studentId}") { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId")
            StudentProfileScreen(navController, studentId ?: "")
        }
        composable("change_password/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ChangePasswordScreen(navController, username)
        }
        composable("profile_settings") { ProfileSettingsScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }
    }
}

@Preview(showBackground = true)
@Composable
fun MathoriaParentAppPreview() {
    MathoriaTheme {
        MathoriaParentApp()
    }
}
