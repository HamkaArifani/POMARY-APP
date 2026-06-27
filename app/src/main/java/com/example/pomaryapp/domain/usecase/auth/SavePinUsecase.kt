package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.repository.AuthRepository
import javax.inject.Inject

class SavePinUsecase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(pin: String){
        return authRepository.savePin(pin)
    }
}