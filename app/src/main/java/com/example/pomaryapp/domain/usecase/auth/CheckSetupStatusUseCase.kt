package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckSetupStatusUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke(): Flow<Boolean> {
        return authRepository.isSetupCompleted()
    }
}