package com.mobilehealthsports.vaccinepass.util.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mobilehealthsports.vaccinepass.business.models.Reminder
import com.mobilehealthsports.vaccinepass.business.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.time.LocalDateTime

/**
 * Receives the BOOT_COMPLETED action and schedules the alarms after reboot.
 */
class BootReceiver : BroadcastReceiver(), KoinComponent {
    private val reminderRepository: ReminderRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            GlobalScope.launch(Dispatchers.IO) {
                val reminder: List<Reminder> = reminderRepository.getAllReminders()

                reminder.forEach {
                    if (it.reminderDate.isBefore(LocalDateTime.now())) {
                        reminderRepository.deleteReminder(it)
                    } else {
                        AlarmScheduler.scheduleAlarmForReminder(context, it)
                    }
                }
            }
        }
    }
}