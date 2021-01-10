package com.mobilehealthsports.vaccinepass.util.notification

import android.R.attr.delay
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.Reminder
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*
import java.util.Calendar.*


/**
 * Helpers to assist in scheduling alarms for ReminderData.
 */
object AlarmScheduler {

    /**
     * Schedules all the alarms for [Reminder].
     *
     * @param context      current application context
     * @param reminderData ReminderData to use for the alarm
     */
    fun scheduleAlarmForReminder(context: Context, reminderData: Reminder) {

        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // get the PendingIntent for the alarm
        val alarmIntent = createPendingIntent(context, reminderData)

        scheduleAlarm(reminderData, alarmIntent, alarmMgr)
    }

    /**
     * Schedules a single alarm
     */
    private fun scheduleAlarm(reminderData: Reminder, alarmIntent: PendingIntent?, alarmMgr: AlarmManager) {

        val timeTillDate = reminderData.reminderDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeTillDate, alarmIntent)
    }

    /**
     * Creates a [PendingIntent] for the Alarm using the [Reminder]
     *
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     */
    private fun createPendingIntent(context: Context, reminderData: Reminder): PendingIntent? {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.reminder)
            type = "${reminderData.uid}"
            putExtra("id", reminderData.uid)
        }

        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Updates a notification.
     * Note: this just calls [AlarmScheduler.scheduleAlarmForReminder] since
     * alarms with exact matching pending intents will update if they are already set, otherwise
     * call [AlarmScheduler.removeAlarmForReminder] if the alarm date is already before today
     *
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     */
    fun updateAlarmForReminder(context: Context, reminderData: Reminder) {
        if (!reminderData.reminderDate.isBefore(LocalDateTime.now())) {
            AlarmScheduler.removeAlarmForReminder(context, reminderData)
        }
    }

    /**
     * Removes the notification if it was previously scheduled.
     *
     * @param context      current application context
     * @param reminderData ReminderData for the notification
     */
    fun removeAlarmForReminder(context: Context, reminderData: Reminder) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.reminder)
        intent.putExtra("id", reminderData.uid)

        // type must be unique so Intent.filterEquals passes the check to make distinct PendingIntents
        val type = String.format(Locale.getDefault(), "%s", reminderData.uid)

        intent.type = type
        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(alarmIntent)
    }
}