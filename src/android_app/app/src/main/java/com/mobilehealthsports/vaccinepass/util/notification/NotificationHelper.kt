package com.mobilehealthsports.vaccinepass.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mobilehealthsports.vaccinepass.R
import com.mobilehealthsports.vaccinepass.business.models.Reminder
import com.mobilehealthsports.vaccinepass.ui.start.StartActivity

object NotificationHelper {
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        // Safety check the OS version for API 26 and greater.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // Created a unique name for the notification channel. The name and description are displayed in the application’s Notification settings.
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // Create the channel using the NotificationManager.
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createSampleDataNotification(context: Context, title: String, message: String, bigText: String, autoCancel: Boolean) {
        // Create the unique channelId for this app using the package name and app name.
        val channelId = "${context.packageName}-${context.getString(R.string.reminder)}"

        // Build notification
        val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_syringe)
            setContentTitle(title)
            setContentText(message)
            setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            priority = NotificationCompat.PRIORITY_DEFAULT
            setAutoCancel(autoCancel)

            // Create an Intent to launch the MainActivity
            val intent = Intent(context, StartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

            // attach it to the NotificationCompat.Builder
            setContentIntent(pendingIntent)
        }

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1001, notificationBuilder.build())
    }

    /**
     * Builds and returns the [NotificationCompat.Builder] for the group notification for a Reminder type.
     * Could be used for groups of reminders
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun buildGroupNotification(context: Context, reminderData: Reminder, group: String): NotificationCompat.Builder {
        val channelId = "${context.packageName}-${group}"
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_syringe)
            setContentTitle(reminderData.title)
            setContentText(reminderData.place)
            setStyle(NotificationCompat.BigTextStyle().bigText(reminderData.place))
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup("reminder")
        }
    }

    /**
     * Builds and returns the NotificationCompat.Builder for the Reminder notification.
     *
     * @param context current application context
     * @param reminderData ReminderData for this notification
     */
    private fun buildNotificationForReminder(context: Context, reminderData: Reminder): NotificationCompat.Builder {
        val channelId = "${context.packageName}-${context.getString(R.string.reminder)}"

        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_syringe)
            setContentTitle(reminderData.title)
            setAutoCancel(true)

            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_syringe))
            setContentText(reminderData.place)
            setGroup("reminder")

            // Launches the app, optionally we could do sth with the reminder id
            val intent = Intent(context, StartActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra("id", reminderData.uid)
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
    }

    /**
     * Creates a notification for [Reminder] with a full notification tap and a single action.
     *
     * @param context      current application context
     * @param reminderData ReminderData for this notification
     */

    fun createNotificationForReminder(context: Context, reminderData: Reminder) {

        // create a group notification
        // val groupBuilder = buildGroupNotification(context, reminderData, group)

        // create the reminder notification
        val notificationBuilder = buildNotificationForReminder(context, reminderData)

        // call notify for both the group and the reminder notification
        val notificationManager = NotificationManagerCompat.from(context)
        //notificationManager.notify("reminder", groupBuilder.build())
        notificationManager.notify(reminderData.uid.toInt(), notificationBuilder.build())
    }
}