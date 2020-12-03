package com.mobilehealthsports.vaccinepass.business.database.converter

import androidx.room.TypeConverter
import java.util.*

class Converter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}