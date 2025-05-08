package com.rokku.mathoria.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow

class NotificationsViewModel : ViewModel() {
    private val database = Firebase.database.reference
    private val _notifications = MutableStateFlow<List<String>>(emptyList())
    val notifications: StateFlow<List<String>> = _notifications.asStateFlow()

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        database.child("notifications").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messages = snapshot.children.map { it.value.toString() }
                _notifications.value = messages
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error (optional)
            }
        })
    }
}