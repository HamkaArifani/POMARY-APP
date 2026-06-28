package com.example.pomaryapp.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

object WhatsAppHelper {
    fun launchWhatsApp(context: Context, phone: String?, text: String){
        val formattedPhone = phone?.replace("[^0-9]".toRegex(), "") ?: ""

        val uri = "https://api.whatsapp.com/send?phone=$formattedPhone&text=${Uri.encode(text)}".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)

        context.startActivity(intent)
    }
}