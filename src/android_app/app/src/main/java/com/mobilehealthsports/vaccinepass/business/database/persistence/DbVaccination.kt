package com.mobilehealthsports.vaccinepass.business.database.persistence

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "vaccination",
    foreignKeys = [ForeignKey(
        entity = DbVaccine::class,
        parentColumns = ["uid"],
        childColumns = ["f_uid"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class DbVaccination(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "f_uid", index = true) val f_uid: Int,
        @ColumnInfo(name = "active") val active: Boolean,
        @ColumnInfo(name = "refresh_date") val refreshDate: LocalDate?,
        @ColumnInfo(name = "vaccination_date") val vaccinationDate: LocalDate?,
)
