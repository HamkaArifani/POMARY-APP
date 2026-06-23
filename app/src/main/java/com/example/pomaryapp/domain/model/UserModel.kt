package com.example.pomaryapp.domain.model

import com.example.pomaryapp.core.utils.Constants

data class UserModel (
    val userId: String,
    val name: String,
    val email: String,
    val pin: String,
    val messageTemplate: String = Constants.DEFAULT_TEMPLATE,
    val hasCompletedSetup: Boolean = false
)