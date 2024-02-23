package io.github.kmichaelk.unnandroid.models.schedule

import java.util.Date

data class ScheduleDay(
    val date: Date,
    val lessons: List<ScheduleLesson>
)
