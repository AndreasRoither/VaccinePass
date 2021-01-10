package com.mobilehealthsports.vaccinepass.business.database.converter

import android.os.Build
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalField

class Converter {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    @TypeConverter
    fun toDate(dateLong: Long?): LocalDate? {
        return dateLong?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun toDateTime(dateString: String?): LocalDateTime? {
        if (dateString == null) return null

        return LocalDateTime.parse(dateString, dateTimeFormatter)
    }

    @TypeConverter
    fun fromDateTime(date: LocalDateTime?): String? {
        if (date == null) return null

        val parsedDate = LocalDateTime.parse(date.toString(), DateTimeFormatter.ISO_DATE_TIME)
        return parsedDate.format(dateTimeFormatter)
    }
}