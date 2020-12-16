package com.mobilehealthsports.vaccinepass.business.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbVaccination
import io.reactivex.rxjava3.core.Flowable


@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccination")
    fun getAll(): Flowable<List<DbVaccination>>

    @Query("SELECT * FROM vaccination WHERE active == 1")
    fun getAllActive(): Flowable<List<DbVaccination>>

    @Query("SELECT * FROM vaccination WHERE uid IN (:vaccineIds)")
    fun loadAllByIds(vaccineIds: LongArray): Flowable<List<DbVaccination>>

    @Insert
    fun insertAll(vararg dbVaccine: DbVaccination): List<Long>

    @Delete
    fun delete(vaccine: DbVaccination)
}
