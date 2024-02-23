package io.github.kmichaelk.unnandroid.models.schedule

data class Schedule(
    val range: ScheduleDateRange,
    val days: List<ScheduleDay>
)