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