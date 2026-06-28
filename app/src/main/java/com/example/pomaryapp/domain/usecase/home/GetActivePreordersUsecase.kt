package com.example.pomaryapp.domain.usecase.home

import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class GetActivePreordersUsecase @Inject constructor(private val preorderRepository: PreorderRepository) {
    operator fun invoke(): Flow<List<PreorderModel>>{
        return preorderRepository.getActivePreorders()
        Timber.d("Mengirimkan preoerder aktif!")
    }
    fun getActivePreorders(): Flow<List<PreorderModel>> {
        return preorderRepository.getActivePreorders()
        Timber.d("Mengirimkan preoerder aktif!")
    }

    fun getCompletedPreorders(): Flow<List<PreorderModel>> {
        return preorderRepository.getCompletedPreorders()
        Timber.d("Mengirimkan preoerder selesai!")
    }
}