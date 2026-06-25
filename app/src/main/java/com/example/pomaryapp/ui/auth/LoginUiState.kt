package com.example.pomaryapp.ui.auth

import com.example.pomaryapp.domain.model.UserModel

data class LoginUiState (
    val isLoading: Boolean = false,
    val user: UserModel? = null,
    val error: String? = null
)