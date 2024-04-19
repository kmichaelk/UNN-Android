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

import android.content.SharedPreferences
import io.github.kmichaelk.unnandroid.api.JournalClient
import io.github.kmichaelk.unnandroid.api.auth.AuthDataHolder
import io.github.kmichaelk.unnandroid.api.auth.JournalAuthInterceptor
import io.github.kmichaelk.unnandroid.api.extractors.JournalTimetableExtractor
import io.github.kmichaelk.unnandroid.api.service.JournalService
import io.github.kmichaelk.unnandroid.converters.JournalServiceConverterFactory
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionTimetable
import io.github.kmichaelk.unnandroid.network.UserAgentInjectInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

class JournalClientImpl @Inject constructor(
    @Named("auth") private val authPrefs: SharedPreferences,
    @Named("base") baseHttpClient: OkHttpClient,
    authDataHolder: AuthDataHolder,
) : JournalClient {

    private val service: JournalService
    private val timetableExtractor = JournalTimetableExtractor()

    init {
        val client = baseHttpClient.newBuilder()
            .followRedirects(false)
            .addInterceptor(UserAgentInjectInterceptor(UserAgentInjectInterceptor.CHROME_ON_WINDOWS))
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(JournalService.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())

        val authenticator = JournalAuthInterceptor(
            retrofit.build().create(JournalService::class.java),
            authPrefs,
            authDataHolder
        )

        service = retrofit
            .addConverterFactory(JournalServiceConverterFactory.create())
            .client(
                client.newBuilder()
                    .addInterceptor(authenticator)
                    .build()
            )
            .build()
            .create(JournalService::class.java)
    }

    private fun getLogin() = authPrefs.getString(JournalAuthInterceptor.PREF_LOGIN_C, null)!!

    override suspend fun getSectionsStats(): List<JournalSectionSemesterStat>
        = service.getSectionsStats()

    override suspend fun getSectionsFilterConstraints(date: Date): JournalSectionFilterConstraints =
        service.getSectionsTimetableMenu(
            date = date,
            type = JournalService.SectionsTimetableRequestType.Week.queryId,
            view = JournalService.SectionsTimetableRequestView.Standard.queryId,
            user = getLogin(),
        )

    override suspend fun getSectionsTimetable(date: Date): JournalSectionTimetable {
        val extendedRaw = service.getSectionsTimetable(
            date = date,
            type = JournalService.SectionsTimetableRequestType.Day.queryId,
            view = JournalService.SectionsTimetableRequestView.List.queryId,
            user = getLogin(),
        )
        val standardRaw = service.getSectionsTimetable(
            date = date,
            type = JournalService.SectionsTimetableRequestType.Day.queryId,
            view = JournalService.SectionsTimetableRequestView.Standard.queryId,
            user = getLogin(),
        )

        return JournalSectionTimetable(
            date = date,
            sections = timetableExtractor.extract(extendedRaw, standardRaw).groupBy {
                it.timespan
            }
        )
    }

    override suspend fun getSectionInfo(id: String): JournalSectionInfo =
        service.getSectionInfo(id).apply {
            this.id = id
        }

    override suspend fun setSectionStatus(id: String, status: Boolean): Boolean =
        service.setSectionStatus(
            id,
            getLogin(),
            if (status)
                JournalService.SectionsSubmitType.Enroll.queryId
            else JournalService.SectionsSubmitType.Remove.queryId
        ).contains("Успешно")
}