package com.example.pomaryapp.service.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.pomaryapp.domain.repository.PreorderRepository
import com.example.pomaryapp.service.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import timber.log.Timber

@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: PreorderRepository
): CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        return try {
            repository.syncWithRemote()

            val activePOs = repository.getActivePreordersSync()
            Timber.d("WORKER_CHECK: Berhasil mengambil ${activePOs.size} data preorder lokal")

            val now = System.currentTimeMillis()
            val oneDayInMillis = 24 * 60 * 60 * 1000L

            activePOs.forEach { po ->
                val diff = po.endDate - now
                val hoursLeft = diff / 3600000
                Timber.d("WORKER_CHECK: PO ${po.title} berakhir dalam $hoursLeft jam")

                if (diff in 0..oneDayInMillis) {
                    Timber.d("WORKER_CHECK: Mengirim notifikasi untuk ${po.title}")
                    NotificationHelper.showDeadlineNotification(context, po.title)
                }
            }
            Result.success()
        }catch (e: Exception) {
            Result.retry()
        }
    }
}