// StudentProfileScreen.kt
package com.rokku.mathoria.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rokku.mathoria.viewmodel.ParentViewModel

@Composable
fun StudentProfileScreen(navController: NavController, studentId: String?) {
    val parentViewModel: ParentViewModel = viewModel()
    val student = parentViewModel.getStudentById(studentId).collectAsState(null).value

    if (student == null) {
        Text("Loading student data...", style = MaterialTheme.typography.bodyLarge)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "${student.firstName} ${student.lastName}", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Math Level: ${student.playerProfile.mathLevel}")
            Text(text = "Game Level: ${student.playerProfile.gameLevel}")
            Text(text = "Coins: ${student.playerProfile.coins}")

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("achievements/${student.uid}") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Achievements")
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { navController.navigate("notifications") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View Notifications")
            }
        }
    }
}
