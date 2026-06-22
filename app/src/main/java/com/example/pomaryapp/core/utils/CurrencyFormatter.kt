package com.example.pomaryapp.core.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    fun format(amount: Long?): String{
        val localeID = Locale.forLanguageTag("id-ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

        val value = amount ?: 0.0

        return numberFormat.format(value).replace("Rp ", "Rp")
    }
    fun format(amount: Double?): String = format(amount?.toLong())
}
fun Long?.toRupiah(): String = CurrencyFormatter.format(this)
fun Double?.toRupiah(): String = CurrencyFormatter.format(this)
fun Int?.toRupiah(): String = CurrencyFormatter.format(this?.toLong())