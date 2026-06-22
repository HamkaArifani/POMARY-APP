package com.example.pomaryapp.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormatter {
    private const val DEFAULT_PATTERN = "dd MMM yyyy"
    private val localeID = Locale.forLanguageTag("id-ID")

    fun formatMillisToDateString(millis: Long?, pattern: String = DEFAULT_PATTERN): String {
        if (millis == null) return "-"
        val sdf = SimpleDateFormat(pattern, localeID)
        return sdf.format(Date(millis))
    }

    fun formatDateRange(startDate: Long?, endDate: Long?): String {
        val start = formatMillisToDateString(startDate)
        val end = formatMillisToDateString(endDate)
        return "$start - $end"
    }
}