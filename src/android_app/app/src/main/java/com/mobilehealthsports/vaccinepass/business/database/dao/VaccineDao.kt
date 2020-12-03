package com.mobilehealthsports.vaccinepass.business.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbVaccine
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface VaccineDao {
    @Query("SELECT * FROM vaccine")
    fun getAll(): Flowable<List<DbVaccine>>

    @Query("SELECT * FROM vaccine WHERE uid IN (:vaccineIds)")
    fun loadAllByIds(vaccineIds: IntArray): Flowable<List<DbVaccine>>

    @Query("SELECT * FROM vaccine WHERE name LIKE :name LIMIT 1")
    fun findByName(name: String): Maybe<DbVaccine>

    @Insert
    fun insertAll(vararg dbVaccine: DbVaccine)

    @Delete
    fun delete(vaccine: DbVaccine)
}
