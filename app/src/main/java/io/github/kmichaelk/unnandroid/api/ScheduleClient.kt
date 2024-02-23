package io.github.kmichaelk.unnandroid.api

import io.github.kmichaelk.unnandroid.models.schedule.Schedule
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleDateRange
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleLesson
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleScope

interface ScheduleClient {

    suspend fun loadSchedule(
        scope: ScheduleScope,
        id: String,
        range: ScheduleDateRange,
        lessonTransformer: ((lesson: ScheduleLesson) -> ScheduleLesson)? = null
    ): Schedule

    suspend fun loadSchedule(
        entity: ScheduleEntity,
        range: ScheduleDateRange,
        lessonTransformer: ((lesson: ScheduleLesson) -> ScheduleLesson)? = null
    ): Schedule

    suspend fun searchEntity(
        term: String,
        scope: ScheduleScope? = null
    ): List<ScheduleEntity>
}