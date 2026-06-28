package com.example.pomaryapp.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.domain.usecase.auth.GetSessionDataUseCase
import com.example.pomaryapp.domain.usecase.auth.LogoutUseCase
import com.example.pomaryapp.domain.usecase.auth.UpdateNameUseCases
import com.example.pomaryapp.domain.usecase.auth.UpdatePinUseCase
import com.example.pomaryapp.domain.usecase.auth.UpdateTemplateUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSessionDataUseCase: GetSessionDataUseCase,
    private val updateNameUseCases: UpdateNameUseCases,
    private val updateTemplateUseCases: UpdateTemplateUseCases,
    private val updatePinUseCase: UpdatePinUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    var name by mutableStateOf("")
    var messageTemplate by mutableStateOf("")

    var showPinDialog by mutableStateOf(false)
    var newPinInput by mutableStateOf("")

    init {
        viewModelScope.launch {
            getSessionDataUseCase().collect { user ->
                name = user.name
                messageTemplate = user.messageTemplate
            }
        }
    }

    fun saveProfile(onSuccess: () -> Unit) {
        viewModelScope.launch {
            updateNameUseCases(name)
            updateTemplateUseCases(messageTemplate)
            onSuccess()
        }
    }

    fun updatePin(onSuccess: () -> Unit) {
        if (newPinInput.length == 4) {
            viewModelScope.launch {
                updatePinUseCase(newPinInput)
                showPinDialog = false
                newPinInput = ""
                onSuccess()
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            logoutUseCase()
            onSuccess()
        }
    }
}