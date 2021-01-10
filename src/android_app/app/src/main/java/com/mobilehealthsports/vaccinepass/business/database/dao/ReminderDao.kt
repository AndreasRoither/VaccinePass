package com.mobilehealthsports.vaccinepass.business.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbReminder
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ReminderDao {
    @Query("SELECT * from reminder")
    fun getAll(): Flowable<List<DbReminder>>

    @Query("SELECT * FROM reminder WHERE uid IN (:reminderIds)")
    fun loadAllByIds(reminderIds: LongArray): Flowable<List<DbReminder>>

    @Insert
    fun insertAll(vararg dbReminder: DbReminder): List<Long>

    @Delete
    fun delete(reminder: DbReminder)

    @Query("DELETE FROM reminder")
    fun deleteAll()
}