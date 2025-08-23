package com.king.behemoth

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Behemoth Slayer"
        val message = intent.getStringExtra("message") ?: "Habit time"
        NotificationHelper.notify(context, title, message)
    }
}
