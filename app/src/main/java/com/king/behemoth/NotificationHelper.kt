package com.king.behemoth

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    private const val CHANNEL_ID = "behemoth_reminders"
    private const val CHANNEL_NAME = "Behemoth Reminders"
    private const val CHANNEL_DESC = "Reminder notifications for Behemoth Slayer"

    fun toast(ctx: Context, msg: String) {
        Toast.makeText(ctx.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun notify(ctx: Context, title: String, message: String, id: Int = System.currentTimeMillis().toInt()) {
        try {
            ensureChannel(ctx)
            val smallIcon = getSafeSmallIcon(ctx)
            val builder = NotificationCompat.Builder(ctx, CHANNEL_ID)
                .setContentTitle(title.ifBlank { "Behemoth Slayer" })
                .setContentText(message.ifBlank { "Time for your habit!" })
                .setSmallIcon(smallIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
            NotificationManagerCompat.from(ctx).notify(id, builder.build())
        } catch (e: Exception) {
            toast(ctx, "Notification error: ${e.message}")
        }
    }

    private fun ensureChannel(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            ).apply { description = CHANNEL_DESC }
            (ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    private fun getSafeSmallIcon(ctx: Context): Int {
        val res = ctx.resources; val pkg = ctx.packageName
        val idLauncherFg = res.getIdentifier("ic_launcher_foreground", "drawable", pkg)
        if (idLauncherFg != 0) return idLauncherFg
        val idBell = res.getIdentifier("ic_notification", "drawable", pkg)
        if (idBell != 0) return idBell
        return android.R.drawable.ic_dialog_info
        }
}
