package com.king.behemoth

import java.util.Calendar

object HabitDefaults {
    // ✏️ Edit times/days here
    val list: List<Habit> = listOf(
        Habit("Wake", 7, 0),
        Habit("Sleep", 23, 0),
        Habit("Shower", 7, 30),
        Habit("Laundry", 10, 0, setOf(Calendar.SATURDAY)),
        Habit("Groceries", 11, 0, setOf(Calendar.SUNDAY)),
        Habit("Car wash", 17, 0, setOf(Calendar.SUNDAY)),
        Habit("Haircut (placeholder monthly)", 14, 0, setOf(Calendar.SATURDAY)), // simple for now
        Habit("Gym", 18, 0, setOf(Calendar.FRIDAY, Calendar.SATURDAY))
    )

    fun prettyList(): String = list.joinToString(" • ") { h ->
        val hh = "%02d".format(h.hour)
        val mm = "%02d".format(h.minute)
        val days = if (h.daysOfWeek.size == 7) "Daily"
                   else h.daysOfWeek.joinToString("/") { d -> shortDay(d) }
        "${h.name} $hh:$mm ($days)"
    }

    private fun shortDay(d: Int): String = when (d) {
        Calendar.MONDAY -> "Mon"
        Calendar.TUESDAY -> "Tue"
        Calendar.WEDNESDAY -> "Wed"
        Calendar.THURSDAY -> "Thu"
        Calendar.FRIDAY -> "Fri"
        Calendar.SATURDAY -> "Sat"
        Calendar.SUNDAY -> "Sun"
        else -> "?"
    }
}
