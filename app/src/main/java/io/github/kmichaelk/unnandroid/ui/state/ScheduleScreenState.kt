package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.schedule.Schedule
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleDateRange
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity

data class ScheduleScreenState(
    val entity: ScheduleEntity? = null,
    val range: ScheduleDateRange,
    val schedule: Schedule? = null,
    val error: UiError? = null,
)
