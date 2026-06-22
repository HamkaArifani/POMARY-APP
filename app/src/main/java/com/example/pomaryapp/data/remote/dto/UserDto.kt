package com.example.pomaryapp.data.remote.dto

data class UserDto (
    val userId: String = "",
    val name: String = "",
    val email: String = "",
    val pin: String = "",
    val hasCompletedSetup: Boolean = false
)