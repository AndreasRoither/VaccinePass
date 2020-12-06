package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.annotation.ColorRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobilehealthsports.vaccinepass.business.models.User
import java.util.*

@Entity(tableName = "user")
data class DbUser(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "first_name") val firstName: String?,
    @ColumnInfo(name = "last_name") val lastName: String?,
    @ColumnInfo(name = "blood_type") val bloodType: String?,
    @ColumnInfo(name = "birth_day") val birthDay: Date?,
    @ColumnInfo(name = "weight") val weight: Float?,
    @ColumnInfo(name = "height") val height: Float?,
    @ColumnInfo(name = "theme_color") val themeColor: Int,
)

