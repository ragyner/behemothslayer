package com.king.behemoth

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Show a real UI so you don't get a black screen
        setContentView(R.layout.activity_main)

        // Buttons
        findViewById<Button>(R.id.btnSchedule).setOnClickListener {
            ReminderScheduler.scheduleAll(this)
        }
        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            ReminderScheduler.cancelAll(this)
            NotificationHelper.toast(this, "Reminders cancelled")
        }

        // Ask for POST_NOTIFICATIONS (Android 13+). If denied, we still run.
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        // Ask user to enable "Exact alarms" if needed (Android 12+).
        ensureExactAlarmPermission()
    }

    private fun ensureExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!am.canScheduleExactAlarms()) {
                try {
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                    NotificationHelper.toast(this, "Enable \"Allow exact alarms\" for Behemoth Slayer.")
                } catch (_: Exception) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                    NotificationHelper.toast(this, "Open \"Alarms & reminders\" and enable exact alarms.")
                }
            }
        }
    }
}
