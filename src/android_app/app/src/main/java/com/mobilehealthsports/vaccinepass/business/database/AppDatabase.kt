package com.mobilehealthsports.vaccinepass.business.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobilehealthsports.vaccinepass.business.database.converter.Converter
import com.mobilehealthsports.vaccinepass.business.database.dao.*
import com.mobilehealthsports.vaccinepass.business.database.persistence.*

@Database(entities = [DbUser::class, DbVaccine::class, DbVaccination::class, DbReminder::class, DbAppointment::class], version = 1)
@TypeConverters(Converter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun vaccineDao(): VaccineDao
    abstract fun userVaccinationDao(): VaccinationDao
    abstract fun reminderDao(): ReminderDao
    abstract fun appointmentDao(): AppointmentDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .createFromAsset("database/init.db")
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}