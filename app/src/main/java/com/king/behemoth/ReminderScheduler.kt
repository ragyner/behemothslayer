package com.king.behemoth

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

object ReminderScheduler {

    private const val TAG = "ReminderScheduler"

    fun scheduleAll(ctx: Context) {
        cancelAll(ctx) // clear first
        HabitDefaults.list.forEachIndexed { index, habit ->
            scheduleNext(ctx, habit, index)
        }
        NotificationHelper.toast(ctx, "Reminders scheduled")
    }

    fun cancelAll(ctx: Context) {
        HabitDefaults.list.forEachIndexed { index, habit ->
            val pi = pendingIntent(ctx, habit, index)
            val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(pi)
        }
    }

    private fun scheduleNext(ctx: Context, habit: Habit, requestCode: Int) {
        val whenMillis = nextTriggerMillis(habit)
        val msg = "Time for ${habit.name}"
        val intent = Intent(ctx, AlarmReceiver::class.java)
            .putExtra("title", "Behemoth Slayer")
            .putExtra("message", msg)

        // Use AlarmManager.AlarmClockInfo (reliable, exact without special permission)
        val showIntent = Intent(ctx, MainActivity::class.java)
        val acInfo = AlarmManager.AlarmClockInfo(
            whenMillis,
            PendingIntent.getActivity(
                ctx, requestCode, showIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )

        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setAlarmClock(acInfo, pendingIntent(ctx, habit, requestCode))

        Log.d(TAG, "Scheduled ${habit.name} at ${Calendar.getInstance().apply { timeInMillis = whenMillis }.time}")
    }

    private fun nextTriggerMillis(habit: Habit): Long {
        val now = Calendar.getInstance()
        val cal = Calendar.getInstance().apply {
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            set(Calendar.HOUR_OF_DAY, habit.hour)
            set(Calendar.MINUTE, habit.minute)
        }

        // If today is allowed and time not passed, keep today; else roll forward to next allowed day
        repeat(8) { // up to a week
            val day = cal.get(Calendar.DAY_OF_WEEK)
            val validToday = habit.daysOfWeek.contains(day)
            val inFuture = cal.timeInMillis > now.timeInMillis
            if (validToday && inFuture) return cal.timeInMillis
            cal.add(Calendar.DAY_OF_MONTH, 1)
            // normalize to same hour:minute each day
            cal.set(Calendar.HOUR_OF_DAY, habit.hour)
            cal.set(Calendar.MINUTE, habit.minute)
        }
        // fallback (+5 minutes)
        return now.timeInMillis + 5 * 60 * 1000
    }

    private fun pendingIntent(ctx: Context, habit: Habit, requestCode: Int): PendingIntent {
        val intent = Intent(ctx, AlarmReceiver::class.java)
            .putExtra("title", "Behemoth Slayer")
            .putExtra("message", "Time for ${habit.name}")
        return PendingIntent.getBroadcast(
            ctx, requestCode, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}
