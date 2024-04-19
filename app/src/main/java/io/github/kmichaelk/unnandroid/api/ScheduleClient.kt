/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

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