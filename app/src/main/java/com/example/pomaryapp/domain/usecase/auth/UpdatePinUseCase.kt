package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.repository.AuthRepository
import javax.inject.Inject

class UpdatePinUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(newPin: String) {
        return repository.updatePin(newPin)
    }
}