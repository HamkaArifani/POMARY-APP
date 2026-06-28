package com.example.pomaryapp.domain.usecase.preorder

import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import javax.inject.Inject

class GetPreorderDetailUseCase @Inject constructor(private val preorderRepository: PreorderRepository) {
    suspend operator fun invoke(id: String): PreorderModel?{
        return preorderRepository.getPreordersById(id)
    }
}