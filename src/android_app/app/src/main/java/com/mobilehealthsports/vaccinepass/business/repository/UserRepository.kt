package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.User

interface UserRepository {
    suspend fun getUser(id: Long): User?
    suspend fun getAllUsers(): List<User>
    suspend fun getUserByNames(firstName: String, lastName: String): User?
    suspend fun insertUser(user: User): Long?
    suspend fun deleteUser(user: User)
    suspend fun deleteAllUser()
}