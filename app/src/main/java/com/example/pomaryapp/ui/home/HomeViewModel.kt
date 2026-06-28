package com.example.pomaryapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.domain.usecase.home.GetActivePreordersUsecase
import com.example.pomaryapp.domain.usecase.home.GetCompletedPreordersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getActivePreordersUsecase: GetActivePreordersUsecase,
    private val getCompletedPreordersUseCase: GetCompletedPreordersUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData(){
        viewModelScope.launch {
            getActivePreordersUsecase()
                .combine(getCompletedPreordersUseCase()){ active, completed ->
                    HomeUiState(
                        isLoading = false,
                        activePreorders = active,
                        completedPreorders = completed
                    )
                }
                .catch { e ->
                    Timber.e(e, "Gagal Memuat Data Preorder")
                    _uiState.value = HomeUiState(isLoading = false, errorMessage = e.message)
                }
                .collect { newState ->
                    _uiState.value = newState
                }
        }

    }
}