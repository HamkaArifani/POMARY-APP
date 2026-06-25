package com.example.pomaryapp.ui.pin

import com.example.pomaryapp.core.utils.StringText

data class PinUiState (
    val isLoading: Boolean = false,
    val isVerified: Boolean = false,
    val error: StringText? = null
)