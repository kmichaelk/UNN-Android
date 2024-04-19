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

package io.github.kmichaelk.unnandroid.api.impl

import com.google.gson.GsonBuilder
import io.github.kmichaelk.unnandroid.api.ScheduleClient
import io.github.kmichaelk.unnandroid.api.service.ScheduleService
import io.github.kmichaelk.unnandroid.converters.ScheduleServiceConverterFactory
import io.github.kmichaelk.unnandroid.models.schedule.Schedule
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleDateRange
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleDay
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleLesson
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleScope
import io.github.kmichaelk.unnandroid.network.UserAgentInjectInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.stream.Collectors
import javax.inject.Inject
import javax.inject.Named

class ScheduleClientImpl @Inject constructor(
    @Named("caching") httpClient: OkHttpClient
) : ScheduleClient {

    private val service: ScheduleService

    init {
        service = Retrofit.Builder()
            .addConverterFactory(ScheduleServiceConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat(ScheduleService.DATE_FORMAT).create()
                )
            )
            .baseUrl(ScheduleService.BASE_URL)
            .client(
                httpClient.newBuilder()
                    .addInterceptor(UserAgentInjectInterceptor(UserAgentInjectInterceptor.CHROME_ON_WINDOWS))
                    .build()
            )
            .build()
            .create(ScheduleService::class.java)
    }

    override suspend fun loadSchedule(
        scope: ScheduleScope,
        id: String,
        range: ScheduleDateRange,
        lessonTransformer: ((lesson: ScheduleLesson) -> ScheduleLesson)?
    ): Schedule {
        val response = service.getSchedule(scope, id, range.begin, range.end)

        val lessons = response.body()!!
        val days = lessons.stream()
            .map { lessonTransformer?.invoke(it) ?: it }
            .collect(
                Collectors.toMap(
                    { l -> l.date },
                    { l ->
                        val day = Pair<Date, MutableList<ScheduleLesson>>(l.date, mutableListOf())
                        day.second.add(l)
                        day
                    },
                    { existing, replacement ->
                        existing.second.addAll(replacement.second)
                        existing
                    })
            )
            .values
            .map { ScheduleDay(it.first, it.second) }
            .sortedWith(Comparator.comparing { it.date })
        return Schedule(range, days)
    }

    override suspend fun loadSchedule(
        entity: ScheduleEntity,
        range: ScheduleDateRange,
        lessonTransformer: ((lesson: ScheduleLesson) -> ScheduleLesson)?
    ): Schedule =
        loadSchedule(entity.scope, entity.id, range, lessonTransformer)

    override suspend fun searchEntity(
        term: String,
        scope: ScheduleScope?
    ): List<ScheduleEntity> = service.search(term, scope)
}