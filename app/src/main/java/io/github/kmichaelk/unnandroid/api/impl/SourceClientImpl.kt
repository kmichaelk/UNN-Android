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