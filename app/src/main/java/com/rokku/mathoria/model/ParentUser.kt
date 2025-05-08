package com.rokku.mathoria.model

data class ParentUser(
    val uid: String = "",
    val username: String = "",
    val password: String = "",
    val mustChangePassword: Boolean = false,
    val role: String = "",
    val linkedChildrenIds: List<String> = emptyList(),
    val firstName: String = "",
    val lastName: String = ""
)
