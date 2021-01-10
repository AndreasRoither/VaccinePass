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
import timber.log.Timber

class AlarmReceiver : BroadcastReceiver(), KoinComponent {
    private val reminderRepository: ReminderRepository by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        Timber.d(TAG, "onReceive() called with: context = [$context], intent = [$intent]")

        context?.let {
            val reminderId = intent?.extras?.getLong("id", -1L) ?: -1

            if (reminderId != -1L) {
                GlobalScope.launch(Dispatchers.IO) {
                    val reminder: Reminder? = reminderRepository.getReminder(reminderId)

                    reminder?.let { r ->
                        GlobalScope.launch(Dispatchers.Main) {
                            NotificationHelper.createNotificationForReminder(context, r)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = AlarmReceiver::class.java.simpleName
    }
}