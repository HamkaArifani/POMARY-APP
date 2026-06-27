package com.example.pomaryapp.ui.home

import com.example.pomaryapp.domain.model.PreorderModel

data class HomeUiState (
    val isLoading: Boolean = false,
    val activePreorders : List<PreorderModel> = emptyList(),
    val completedPreorders: List<PreorderModel> = emptyList(),
    val errorMessage : String? = null
)