package io.github.kmichaelk.unnandroid.models.schedule

import java.util.Calendar
import java.util.Date

data class ScheduleDateRange(
    val begin: Date,
    val end: Date
) {
    companion object {
        fun thisWeek(): ScheduleDateRange {
            val calendar = Calendar.getInstance()
            val begin = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val end = calendar.time

            return ScheduleDateRange(begin, end)
        }
    }
}