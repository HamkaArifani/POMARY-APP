package com.example.pomaryapp.domain.usecase.auth

import com.example.pomaryapp.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateTemplateUseCases @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(template: String){
        return repository.updateMessageTemplate(template)
    }
}