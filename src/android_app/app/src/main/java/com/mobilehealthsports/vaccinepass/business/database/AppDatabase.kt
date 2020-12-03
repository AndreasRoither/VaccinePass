package com.mobilehealthsports.vaccinepass.business.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobilehealthsports.vaccinepass.business.database.converter.Converter
import com.mobilehealthsports.vaccinepass.business.database.dao.UserDao
import com.mobilehealthsports.vaccinepass.business.database.dao.VaccineDao
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbUser
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbVaccine

@Database(entities = [DbUser::class, DbVaccine::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun vaccineDao(): VaccineDao
}
