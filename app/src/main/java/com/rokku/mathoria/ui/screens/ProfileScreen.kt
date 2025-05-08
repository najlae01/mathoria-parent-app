package com.rokku.mathoria.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rokku.mathoria.model.Student
import com.rokku.mathoria.ui.BottomNavigationBar
import com.rokku.mathoria.viewmodel.AuthViewModel
import com.rokku.mathoria.viewmodel.ParentViewModel
import com.rokku.mathoria.viewmodel.ParentViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val authViewModel: AuthViewModel = viewModel()
    val parentViewModel: ParentViewModel = viewModel(factory = ParentViewModelFactory(authViewModel))

    val username = authViewModel.currentUsername.collectAsState().value
    val firstName = authViewModel.currentFirstName.collectAsState().value
    val lastName = authViewModel.currentLastName.collectAsState().value

    println(" Debug: Username in ProfileScreen: $username")

    LaunchedEffect(username) {
        if (username.isNotBlank()) {
            println(" Loading Children for Username: $username")
            parentViewModel.loadLinkedStudents(username)
        }
    }

    val isLoading = parentViewModel.isLoading.collectAsState().value
    val children: List<Student> = parentViewModel.linkedStudents.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mathoria - Parent App") },
                actions = {
                    IconButton(onClick = {
                        navController.navigate("profile_settings")
                    }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isLoading) {
                CircularProgressIndicator()
                Text(text = "Loading...", style = MaterialTheme.typography.bodyLarge)
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(it)) {
                    Text(
                        text = if (firstName.isNotBlank() && lastName.isNotBlank())
                            "$firstName $lastName"
                        else "",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 0.dp)
                    )
                    Text(
                        text = if (username.isNotBlank()) "Username: $username" else "Username Not Available",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 8.dp)
                    )
                    Text(
                        text = "Your Children",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    if (children.isEmpty()) {
                        Text(
                            text = "No students found. Please contact the school.",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 2.dp)
                        )  {
                            items(children.size) { index ->
                                val child = children[index]
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                        .clickable {
                                            navController.navigate("student_profile/${child.uid}")
                                        },
                                    elevation = CardDefaults.cardElevation(4.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.Top,
                                        modifier = Modifier.padding(16.dp)
                                    ) {
                                        Text(
                                            text = child.firstName,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        Text(
                                            text = "Grade: ${child.schoolGrade}",
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

