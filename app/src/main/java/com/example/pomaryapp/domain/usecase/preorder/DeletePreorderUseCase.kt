package com.example.pomaryapp.domain.usecase.preorder

import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import javax.inject.Inject

class DeletePreorderUseCase @Inject constructor(private val preorderRepository: PreorderRepository) {
    suspend operator fun invoke(preorderModel: PreorderModel) {
        return preorderRepository.deletePreorder(preorderModel)
    }
}