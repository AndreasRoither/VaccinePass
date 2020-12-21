package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "user")
data class DbUser(
        @PrimaryKey(autoGenerate = true) val uid: Long,
        @ColumnInfo(name = "first_name") val firstName: String?,
        @ColumnInfo(name = "last_name") val lastName: String?,
        @ColumnInfo(name = "blood_type") val bloodType: String?,
        @ColumnInfo(name = "birth_day") val birthDay: LocalDate?,
        @ColumnInfo(name = "weight") val weight: Float?,
        @ColumnInfo(name = "height") val height: Float?,
        @ColumnInfo(name = "theme_color") val themeColor: Int,
        @ColumnInfo(name = "photo_path") val photoPath: String?,
)

