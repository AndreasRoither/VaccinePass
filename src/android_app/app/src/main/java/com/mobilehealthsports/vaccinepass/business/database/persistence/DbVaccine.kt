package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "vaccine")
data class DbVaccine(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "company") val company: String?,
    @ColumnInfo(name = "indication") val indication: String?,
    @ColumnInfo(name = "target_group") val targetGroup: String?,
    @ColumnInfo(name = "note") val note: String?,
    @ColumnInfo(name = "adjuvans") val adjuvans: String?,
    @ColumnInfo(name = "thiomersal") val thiomersal: String?,
    @ColumnInfo(name = "refresh_recommendation") val refreshRecommendation: String?,
    @ColumnInfo(name = "refresh_date") val refreshDate: Date?,
)

