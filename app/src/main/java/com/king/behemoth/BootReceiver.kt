package com.king.behemoth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action.orEmpty()
        Log.d("BootReceiver", "Received: $action")

        // Recreate notif channel and reschedule all reminders after:
        // - device reboot
        // - time change / timezone change
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == Intent.ACTION_TIME_CHANGED ||
            action == Intent.ACTION_TIMEZONE_CHANGED
        ) {
            NotificationHelper.createChannels(context)
            ReminderScheduler.scheduleAll(context)
        }
    }
}
