// ParentViewModel.kt
package com.rokku.mathoria.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import com.rokku.mathoria.model.Student
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ParentViewModel(private val authViewModel: AuthViewModel) : ViewModel() {
    private val database = Firebase.database.reference

    private val _linkedStudents = MutableStateFlow<List<Student>>(emptyList())
    val linkedStudents = _linkedStudents.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadLinkedStudents(username: String) {
        println("Loading Linked Students")
        _isLoading.value = true

        if (username.isBlank()) {
            println(" Error: Username is blank")
            _linkedStudents.value = emptyList()
            _isLoading.value = false
            return
        }

        database.child("users").orderByChild("username").equalTo(username)
            .get().addOnSuccessListener { snapshot ->
                val parentSnap = snapshot.children.firstOrNull()
                if (parentSnap != null) {
                    val linkedChildrenIds = parentSnap.child("linkedChildrenIds").children.map { it.value.toString() }
                    fetchStudents(linkedChildrenIds)
                } else {
                    println(" No parent found with username: $username")
                    _linkedStudents.value = emptyList() // Parent not found
                    _isLoading.value = false
                }
            }.addOnFailureListener {
                println(" Error loading parent: ${it.message}")
                _linkedStudents.value = emptyList()
                _isLoading.value = false
            }
    }

    private fun fetchStudents(studentIds: List<String>) {
        if (studentIds.isEmpty()) {
            println("No linked children")
            _linkedStudents.value = emptyList()
            _isLoading.value = false
            return
        }

        database.child("users").get().addOnSuccessListener { snapshot ->
            val students = studentIds.mapNotNull { id ->
                val studentSnapshot = snapshot.child(id)
                if (studentSnapshot.exists() && studentSnapshot.child("role").value == "Student") {
                    try {
                        studentSnapshot.getValue(Student::class.java)
                    } catch (e: Exception) {
                        println(" Error mapping student: ${e.message}")
                        null
                    }
                } else {
                    null
                }
            }
            _linkedStudents.value = students
            _isLoading.value = false
            println(" Debug: Loaded students: ${students.map { it.firstName }}")
        }.addOnFailureListener {
            println(" Error fetching students: ${it.message}")
            _linkedStudents.value = emptyList()
            _isLoading.value = false
        }
    }

    // Get a Single Student by ID
    fun getStudentById(studentId: String?): StateFlow<Student?> {
        return _linkedStudents.map { students ->
            students.find { it.uid == studentId }
        }.stateIn(viewModelScope, initialValue = null, started = kotlinx.coroutines.flow.SharingStarted.Eagerly)
    }
}
