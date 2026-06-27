package com.example.pomaryapp.domain.usecase.home

import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.repository.PreorderRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class LoadDataUseCase @Inject constructor(private val repository: PreorderRepository) {
    fun getActivePreorders(): Flow<List<PreorderModel>> {
        return repository.getActivePreorders()
        Timber.d("Mengirimkan preoerder aktif!")
    }

    fun getCompletedPreorders(): Flow<List<PreorderModel>> {
        return repository.getCompletedPreorders()
        Timber.d("Mengirimkan preoerder selesai!")
    }
}