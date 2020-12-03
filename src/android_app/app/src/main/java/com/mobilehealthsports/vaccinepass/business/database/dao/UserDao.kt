package com.mobilehealthsports.vaccinepass.business.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mobilehealthsports.vaccinepass.business.database.persistence.DbUser
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): Flowable<List<DbUser>>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): Flowable<List<DbUser>>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): Maybe<DbUser>

    @Insert
    fun insertAll(vararg dbUsers: DbUser)

    @Delete
    fun delete(user: DbUser)
}
