package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "reminder")
data class DbReminder(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "reminder_date") val reminderDate: LocalDate,
    @ColumnInfo(name="vaccination_uid", index = true) val vaccination_uid: Long
)