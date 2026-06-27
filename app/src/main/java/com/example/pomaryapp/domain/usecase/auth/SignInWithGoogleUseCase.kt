package com.example.pomaryapp.domain.usecase.auth

import android.content.Context
import com.example.pomaryapp.domain.model.UserModel
import com.example.pomaryapp.domain.repository.AuthRepository
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(private val repository: AuthRepository){
    suspend operator fun invoke(context: Context): Result<UserModel?>{
        return repository.signInWithGoogle(context)
    }
}