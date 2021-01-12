package com.mobilehealthsports.vaccinepass.business.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbAppointment
import io.reactivex.rxjava3.core.Flowable

@Dao
interface AppointmentDao {
    @Query("SELECT * FROM appointment")
    fun getAll(): Flowable<List<DbAppointment>>

    @Query("SELECT * FROM appointment WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: LongArray): Flowable<List<DbAppointment>>

    @Query("SELECT * FROM appointment WHERE reminder = 'true'")
    fun getAppointmentsWithReminder(): Flowable<List<DbAppointment>>

    @Insert
    fun insertAll(vararg dbAppointments: DbAppointment): List<Long>

    @Delete
    fun delete(dbAppointment: DbAppointment)

    @Query("DELETE FROM appointment")
    fun deleteAll()
}