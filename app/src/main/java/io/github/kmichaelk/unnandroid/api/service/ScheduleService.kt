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

package io.github.kmichaelk.unnandroid.api.service

import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleLesson
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleScope
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface ScheduleService {

    companion object {
        const val BASE_URL = PortalService.BASE_URL + "ruzapi/"

        const val DATE_FORMAT = "yyyy.MM.dd"
    }

    @GET("schedule/{scope}/{id}")
    suspend fun getSchedule(
        @Path("scope") scope: ScheduleScope,
        @Path("id") id: String,

        @Query("start") start: Date,
        @Query("finish") finish: Date,

        @Query("lng") lng: Int = 1
    ) : Response<List<ScheduleLesson>>

    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("type") scope: ScheduleScope?
    ): List<ScheduleEntity>
}