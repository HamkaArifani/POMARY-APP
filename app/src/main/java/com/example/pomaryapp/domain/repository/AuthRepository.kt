package com.example.pomaryapp.domain.repository

import com.example.pomaryapp.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun signInWithGoogle(): Result<UserModel?>

    fun isUserLoggedIn(): Boolean
    fun getSessionData(): Flow<UserModel>
    fun isSetupCompleted(): Flow<Boolean>

    suspend fun savePin(pin: String)
    suspend fun updatePin(newPin: String)
    suspend fun getPin(): String?
    suspend fun validatePin(inputPin: String): Boolean

    suspend fun updateName(newName: String)
    suspend fun updateMessageTemplate(newTemplate: String)

    suspend fun logout()
}