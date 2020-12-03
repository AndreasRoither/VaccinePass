package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.database.AppDatabase
import com.mobilehealthsports.vaccinepass.business.database.extension.toDb
import com.mobilehealthsports.vaccinepass.business.database.extension.toUser
import com.mobilehealthsports.vaccinepass.business.models.User

class UserRepositoryImpl(private val database: AppDatabase) : UserRepository {
    override suspend fun getUser(id: Int): User? {
        return database.userDao().loadAllByIds(intArrayOf(id)).blockingFirst().firstOrNull()
            ?.toUser()
    }

    override suspend fun getAllUsers(): List<User> {
        return database.userDao().getAll().blockingFirst().map { it.toUser() }
    }

    override suspend fun getUserByNames(firstName: String, lastName: String): User? {
        return database.userDao().findByName(firstName, lastName).blockingGet()?.toUser()
    }

    override suspend fun insertUser(user: User) {
        database.userDao().insertAll(user.toDb())
    }

    override suspend fun deleteUser(user: User) {
        database.userDao().delete(user.toDb())
    }
}