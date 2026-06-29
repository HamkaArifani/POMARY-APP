package com.example.pomaryapp.core.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

object WhatsAppHelper {
    fun launchWhatsApp(context: Context, phone: String?, text: String){
        var formattedPhone = phone?.replace("[^0-9]".toRegex(), "") ?: ""

        if (formattedPhone.startsWith("0")) {
            formattedPhone = "62" + formattedPhone.substring(1)
        }

        val uri = "https://api.whatsapp.com/send?phone=$formattedPhone&text=${Uri.encode(text)}".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)

        context.startActivity(intent)
    }
}