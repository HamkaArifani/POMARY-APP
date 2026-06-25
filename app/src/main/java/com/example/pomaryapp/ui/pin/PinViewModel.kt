package com.example.pomaryapp.ui.pin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(private val authRepository: AuthRepository): ViewModel(){
    var pinState by mutableStateOf(PinUiState())
        private set

    private val _lockoutMinutes = MutableStateFlow(0L)
    val lockoutMinutes = _lockoutMinutes.asStateFlow()

    init {
        monitorLockout()
    }

    fun validatePin(input: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            pinState = pinState.copy(isLoading = true)

            val isValid = authRepository.validatePin(input)

            if (isValid) {
                pinState = pinState.copy(isLoading = false, isVerified = true)
                onSuccess()
            } else {
                pinState = pinState.copy(
                    isLoading = false,
                    error = "PIN yang Anda masukkan salah"
                )
            }
        }
    }

    fun createPin(pin: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            pinState = pinState.copy(isLoading = true)
            authRepository.savePin(pin)
            pinState = pinState.copy(isLoading = false, isVerified = true)
            onSuccess()
        }
    }

    private fun monitorLockout() {
        viewModelScope.launch {
            authRepository.getLockoutRemainingTime().collect { remainingMinutes ->
                _lockoutMinutes.value = remainingMinutes
                if (remainingMinutes > 0) {
                    pinState = pinState.copy(
                        error = "Terlalu banyak percobaan. Terkunci $remainingMinutes menit."
                    )
                }
            }
        }
    }

    fun isSetupCompleted() = authRepository.isSetupCompleted()

}