package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.model.UserModel
import com.example.pomaryapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionDataUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): Flow<UserModel> {
        return repository.getSessionData()
    }
}