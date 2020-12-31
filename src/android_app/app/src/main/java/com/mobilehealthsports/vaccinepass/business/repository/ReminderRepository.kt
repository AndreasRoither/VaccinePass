package com.mobilehealthsports.vaccinepass.business.repository

import com.mobilehealthsports.vaccinepass.business.models.Reminder

interface ReminderRepository {
    suspend fun getReminder(id: Long): Reminder?
    suspend fun getAllReminders(): List<Reminder>
    suspend fun getReminderByVaccination(vaccination_uid: Long): Reminder?
    suspend fun insertReminder(reminder: Reminder): Long?
    suspend fun deleteReminder(reminder: Reminder)
    suspend fun deleteAllReminder()
}