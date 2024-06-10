package com.example.locaweb.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatLocalDate(localDate: LocalDate): String {
    val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
    return "${localDate.dayOfMonth} de $month"
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatLocalDate(localDate: LocalDateTime): String {
    val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
    return "${localDate.dayOfMonth} de $month"
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatLocalDate(localDate: LocalDate, withYear: Boolean): String {
    val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
    return "${localDate.dayOfMonth} de $month de ${localDate.year}"
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatLocalDate(localDate: LocalDateTime, withYear: Boolean): String {
    val month = localDate.month.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
    return "${localDate.dayOfMonth} de $month de ${localDate.year}"
}

fun getHour(localDate: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return localDate.format(formatter)
}

fun convertDateToMilliseconds(localDateTime: LocalDateTime): Long {
    val zonaId = ZoneId.systemDefault()
    val instant = localDateTime.atZone(zonaId).toInstant()
    return instant.toEpochMilli()
}

fun convertMillisecondsToDate(timestamp: Long): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochMilli(timestamp),
        ZoneId.systemDefault()
    )
}

fun convertMillisecondsToLocalDate(timestamp: Long): LocalDate {
    val instant = Instant.ofEpochMilli(timestamp)
    return instant.atZone(ZoneId.systemDefault()).toLocalDate()

}