package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateNameUseCases @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(name: String){
        return repository.updateName(name)
    }
}