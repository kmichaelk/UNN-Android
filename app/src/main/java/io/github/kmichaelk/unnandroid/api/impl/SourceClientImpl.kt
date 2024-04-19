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

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import io.github.kmichaelk.unnandroid.api.SourceClient
import io.github.kmichaelk.unnandroid.api.auth.AuthDataHolder
import io.github.kmichaelk.unnandroid.api.auth.SourceAuthInterceptor
import io.github.kmichaelk.unnandroid.api.interceptor.SourceJsonNormalizer
import io.github.kmichaelk.unnandroid.api.service.SourceService
import io.github.kmichaelk.unnandroid.converters.SourceServiceConverterFactory
import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import io.github.kmichaelk.unnandroid.models.source.SourceSubject
import io.github.kmichaelk.unnandroid.models.source.SourceWebinar
import io.github.kmichaelk.unnandroid.network.UserAgentInjectInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import javax.inject.Inject
import javax.inject.Named

class SourceClientImpl @Inject constructor(
    @Named("auth") private val authPrefs: SharedPreferences,
    @Named("base") baseHttpClient: OkHttpClient,
    authDataHolder: AuthDataHolder,
) : SourceClient {

    private val service: SourceService

    init {
        val client = baseHttpClient.newBuilder()
            .followRedirects(false)
            .addInterceptor(UserAgentInjectInterceptor(UserAgentInjectInterceptor.CHROME_ON_WINDOWS))
            .addInterceptor(SourceJsonNormalizer())
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(SourceServiceConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat(SourceService.DATE_FORMAT).create()
                )
            )
            .baseUrl(SourceService.BASE_URL)

        val authenticator = SourceAuthInterceptor(
            retrofit.build().create(SourceService::class.java),
            authPrefs,
            authDataHolder
        )

        service = retrofit
            .client(
                client.newBuilder()
                    .addInterceptor(authenticator)
                    .build()
            )
            .build()
            .create(SourceService::class.java)
    }

    override suspend fun getMaterials(semester: Int, year: Int): List<SourceSubject> =
        service.getMaterials(semester, year).map { SourceSubject(it.key, it.value) }

    override suspend fun getWebinars(semester: Int, year: Int): List<SourceWebinar> {
        @SuppressLint("SimpleDateFormat")
        val format = SimpleDateFormat(SourceService.DATE_FORMAT)
        return service.getWebinars(semester, year).map {
            it.parsedDate = format.parse("${it.dateStr} ${it.timeStr}")!!
            it
        }
    }

    override suspend fun getSemesters(): List<SourceSemester> =
        service.getSemesters()
}