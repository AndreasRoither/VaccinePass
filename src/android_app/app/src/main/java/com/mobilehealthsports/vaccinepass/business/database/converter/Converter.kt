package com.mobilehealthsports.vaccinepass.business.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class Converter {
    @TypeConverter
    fun toDate(dateLong: Long?): LocalDate? {
        return dateLong?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromDate(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}