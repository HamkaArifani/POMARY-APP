package com.example.pomaryapp.ui.pin

data class PinUiState (
    val isLoading: Boolean = false,
    val isVerified: Boolean = false,
    val error: String? = null
)