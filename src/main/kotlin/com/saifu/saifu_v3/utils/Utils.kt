package com.saifu.saifu_v3.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String.toLocalDate(dateTimeFormatter: DateTimeFormatter): LocalDate {
    return LocalDate.parse(this, dateTimeFormatter)
}

fun LocalDate.between(from: LocalDate, to: LocalDate): Boolean {
    return !(from.isAfter(this) || to.isBefore(this))
}

class Utils {
    companion object {
        const val DATE_TIME_FORMAT = "yyyy-MM-dd"
        const val ZAIM_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)
        val zaimDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(ZAIM_DATE_TIME_FORMAT)
    }
}
