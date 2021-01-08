package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "appointment")
data class DbAppointment (
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "place") val place: String?,
    @ColumnInfo(name = "appointment_date") val appointment_date: LocalDate?,
    @ColumnInfo(name = "reminder") val reminder: Boolean,
    @ColumnInfo(name = "reminder_date") val reminder_date: LocalDate?
)