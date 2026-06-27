package com.example.pomaryapp.ui.pin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.R
import com.example.pomaryapp.core.utils.StringText
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.domain.usecase.auth.CheckSetupStatusUseCase
import com.example.pomaryapp.domain.usecase.auth.GetLockoutTimeUseCase
import com.example.pomaryapp.domain.usecase.auth.SavePinUsecase
import com.example.pomaryapp.domain.usecase.auth.ValidatePinUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PinViewModel @Inject constructor(
    private val validatePinUseCase: ValidatePinUseCase,
    private val savePinUsecase: SavePinUsecase,
    private val getLockoutTimeUseCase: GetLockoutTimeUseCase,
    private val checkSetupStatusUseCase: CheckSetupStatusUseCase
): ViewModel(){
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

            val isValid = validatePinUseCase(input)

            if (isValid) {
                pinState = pinState.copy(isLoading = false, isVerified = true)
                onSuccess()
            } else {
                pinState = pinState.copy(
                    isLoading = false,
                    error = StringText.StringResource(R.string.wrong_pin)
                )
            }
        }
    }

    fun createPin(pin: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            pinState = pinState.copy(isLoading = true)
            savePinUsecase(pin)
            pinState = pinState.copy(isLoading = false, isVerified = true)
            onSuccess()
        }
    }

    private fun monitorLockout() {
        viewModelScope.launch {
            getLockoutTimeUseCase().collect { remainingMinutes ->
                _lockoutMinutes.value = remainingMinutes
                if (remainingMinutes > 0) {
                    pinState = pinState.copy(
                        error = StringText.StringResource(resId = R.string.lockout_msg, remainingMinutes)
                    )
                }
            }
        }
    }

    fun isSetupCompleted() = checkSetupStatusUseCase()

}