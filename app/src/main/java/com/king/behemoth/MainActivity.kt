package com.king.behemoth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val requestNotifPerm = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { /* result ignored; user can enable later in Settings */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ask for POST_NOTIFICATIONS on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!granted) requestNotifPerm.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        NotificationHelper.createChannels(this)

        // Show habits list text
        findViewById<TextView>(R.id.habitsText).text =
            HabitDefaults.prettyList()

        findViewById<Button>(R.id.btnSchedule).setOnClickListener {
            ReminderScheduler.scheduleAll(this)
        }

        findViewById<Button>(R.id.btnCancel).setOnClickListener {
            ReminderScheduler.cancelAll(this)
        }
    }
}
