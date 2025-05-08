package com.rokku.mathoria.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rokku.mathoria.model.Student

@Composable
fun AchievementsScreen(student: Student) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Achievements", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(student.achievements.badges) { badge ->
                Text(text = "- $badge", style = MaterialTheme.typography.displayLarge)
                HorizontalDivider()
            }
        }
    }
}
