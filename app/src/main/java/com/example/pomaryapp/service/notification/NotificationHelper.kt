package com.example.pomaryapp.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.pomaryapp.R
import timber.log.Timber

object NotificationHelper {
    const val CHANNEL_ID = "preorder_notifications"

    fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = context.getString(R.string.notif_channel_name)
            val descriptionText = context.getString(R.string.notif_channel_desc)

            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showDeadlineNotification(context: Context, poTitle: String){
        val title = context.getString(R.string.notif_deadline_title)
        val message = context.getString(R.string.notif_deadline_msg, poTitle)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        try {
            NotificationManagerCompat.from(context).notify(
                System.currentTimeMillis().toInt(),
                builder.build()
            )
        } catch (e: SecurityException){
            Timber.e(e, "Izin Tidak Diberikan")
        }
    }
}