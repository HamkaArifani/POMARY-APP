package com.example.pomaryapp.domain.model

data class UserModel (
    val userId: String,
    val name: String,
    val email: String,
    val pin: String,
    val hasCompletedSetup: Boolean = false
)