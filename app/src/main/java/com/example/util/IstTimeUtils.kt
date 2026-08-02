package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object IstTimeUtils {

    private val istTimeZone = TimeZone.getTimeZone("GMT+05:30")

    fun getCurrentIstDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.timeZone = istTimeZone
        return sdf.format(Date())
    }

    fun getFormattedDateLabel(dateStr: String): String {
        try {
            val sdfInput = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            sdfInput.timeZone = istTimeZone
            val date = sdfInput.parse(dateStr) ?: Date()
            val sdfOutput = SimpleDateFormat("MMM d", Locale.US)
            sdfOutput.timeZone = istTimeZone
            return sdfOutput.format(date).uppercase()
        } catch (e: Exception) {
            return dateStr
        }
    }

    fun getNext1AmIstMillis(): Long {
        val cal = Calendar.getInstance(istTimeZone)
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)

        val targetCal = Calendar.getInstance(istTimeZone)
        targetCal.set(Calendar.HOUR_OF_DAY, 1)
        targetCal.set(Calendar.MINUTE, 0)
        targetCal.set(Calendar.SECOND, 0)
        targetCal.set(Calendar.MILLISECOND, 0)

        if (currentHour >= 1) {
            targetCal.add(Calendar.DAY_OF_YEAR, 1)
        }

        return targetCal.timeInMillis
    }

    fun getMillisTillNext1AmIst(): Long {
        val diff = getNext1AmIstMillis() - System.currentTimeMillis()
        return if (diff > 0) diff else 0L
    }

    fun formatCountdown(millis: Long): String {
        val totalSecs = millis / 1000
        val hours = totalSecs / 3600
        val mins = (totalSecs % 3600) / 60
        val secs = totalSecs % 60
        return String.format(Locale.US, "%02d:%02d:%02d", hours, mins, secs)
    }

    fun formatShortTime(timestamp: Long?): String {
        if (timestamp == null || timestamp == 0L) return ""
        val sdf = SimpleDateFormat("h:mm a", Locale.US)
        sdf.timeZone = istTimeZone
        return sdf.format(Date(timestamp)).lowercase()
    }
}
