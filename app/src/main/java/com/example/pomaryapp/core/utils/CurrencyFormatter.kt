package com.example.pomaryapp.core.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    fun format(amount: Double?): String{
        val localeID = Locale.forLanguageTag("id-ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)
        numberFormat.maximumFractionDigits = 0

        val value = amount ?: 0.0

        return numberFormat.format(value).replace("Rp ", "Rp")
    }
    fun format(amount: Long?): String = format(amount?.toDouble())
}
fun Double?.toRupiah(): String = CurrencyFormatter.format(this)
fun Long?.toRupiah(): String = CurrencyFormatter.format(this)