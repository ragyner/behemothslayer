package com.king.behemoth

import java.util.Calendar

data class Habit(
    val name: String,
    val hour: Int,
    val minute: Int,
    val daysOfWeek: Set<Int> = setOf( // Calendar.MONDAY..SUNDAY
        Calendar.MONDAY, Calendar.TUESDAY, Calendar.WEDNESDAY,
        Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, Calendar.SUNDAY
    ),
    val note: String = ""
)
