package com.king.behemoth

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // Android 13+ notification permission
    private val requestPostNotifications = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            NotificationHelper.toast(this, "Notifications disabled. Reminders will still schedule.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep your existing UI; no setContentView here to avoid breaking your layout.

        ensureNotificationPermission()
        ensureExactAlarmPermission()   // 👈 This fixes your “needs SCHEDULE_EXACT_ALARM” error
    }

    private fun ensureNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            requestPostNotifications.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun ensureExactAlarmPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!am.canScheduleExactAlarms()) {
                try {
                    // Directly open the exact-alarm permission screen for this app
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                    NotificationHelper.toast(this, "Enable \"Allow exact alarms\" for Behemoth Slayer.")
                } catch (e: Exception) {
                    // Fallback: open app settings page
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
