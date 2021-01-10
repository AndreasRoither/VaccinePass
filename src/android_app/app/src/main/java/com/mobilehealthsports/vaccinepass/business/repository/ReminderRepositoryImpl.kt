package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.database.AppDatabase
import com.mobilehealthsports.vaccinepass.business.database.extension.toDb
import com.mobilehealthsports.vaccinepass.business.database.extension.toReminder
import com.mobilehealthsports.vaccinepass.business.models.Reminder

class ReminderRepositoryImpl(private val database: AppDatabase) : ReminderRepository {
    override suspend fun getReminder(id: Long): Reminder? {
        return database.reminderDao().loadAllByIds(longArrayOf(id)).blockingFirst().firstOrNull()?.toReminder()
    }

    override suspend fun getAllReminders(): List<Reminder> {
        return database.reminderDao().getAll().blockingFirst().map { it.toReminder() }
    }

    override suspend fun insertReminder(reminder: Reminder): Long? {
        return database.reminderDao().insertAll(reminder.toDb()).firstOrNull()
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        database.reminderDao().delete(reminder.toDb())
    }

    override suspend fun deleteAllReminder() {
        database.reminderDao().deleteAll()
    }

}