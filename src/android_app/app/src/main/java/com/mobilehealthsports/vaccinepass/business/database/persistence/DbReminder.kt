package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "reminder")
data class DbReminder(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "reminder_date") val reminderDate: LocalDateTime,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "place") val place: String
)