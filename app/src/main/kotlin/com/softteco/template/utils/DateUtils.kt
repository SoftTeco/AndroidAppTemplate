package com.softteco.template.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

object DateUtils {

    fun isoDateToMillis(date: String): Long {
        val localDate = LocalDate.parse(date)
        val zonedDateTime = localDate.atStartOfDay().atZone(ZoneId.systemDefault())
        return zonedDateTime.toInstant().toEpochMilli()
    }

    fun millisToIsoDate(milliseconds: Long): String {
        val instant = Instant.ofEpochMilli(milliseconds)
        val localDate = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return localDate.format(formatter)
    }

    fun millisToLocalDate(milliseconds: Long): String {
        val instant = Instant.ofEpochMilli(milliseconds)
        val localDate = ZonedDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate()
        val formatter = DateTimeFormatter
            .ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale.getDefault())
        return localDate.format(formatter)
    }

    fun isoDateToLocalDate(date: String): String {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE
        return LocalDate.parse(date, formatter).toString()
    }
}
