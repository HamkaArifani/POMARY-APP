package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.repository.AuthRepository
import javax.inject.Inject

class ValidatePinUseCase @Inject constructor(private val repository: AuthRepository){
    suspend operator fun invoke(pin: String) {
        repository.updatePin(pin)
    }
}