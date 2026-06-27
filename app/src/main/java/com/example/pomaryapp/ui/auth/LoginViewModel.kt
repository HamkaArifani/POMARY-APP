package com.example.pomaryapp.ui.auth

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.R
import com.example.pomaryapp.core.utils.StringText
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.domain.usecase.auth.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val signInWithGoogleUseCase: SignInWithGoogleUseCase): ViewModel() {
    var loginState by mutableStateOf(LoginUiState())
        private set

    fun signIn(context: Context){
        viewModelScope.launch {
            loginState = loginState.copy(isLoading = true)
            signInWithGoogleUseCase(context)
                .onSuccess { user ->
                    loginState = loginState.copy(isLoading = false, user = user)
                }
                .onFailure { e ->
                    loginState = loginState.copy(isLoading = false, error = e.message ?.let { StringText.DynamicString(it)}
                        ?: StringText.StringResource(R.string.fail_to_login)
                    )
                }
        }
    }

}