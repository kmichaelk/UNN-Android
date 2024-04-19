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

package io.github.kmichaelk.unnandroid.converters.journal

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Converter

class JournalSectionInfoConverter : Converter<ResponseBody, JournalSectionInfo> {

    override fun convert(value: ResponseBody): JournalSectionInfo {
        val doc: Document
        value.use { doc = Jsoup.parse(it.string()) }

        val type: String
        val time: String
        val trainer: String
        val trainerUrl: String
        val auditorium: String
        val groups: String
        val faculty: String
        val healthGroups: String
        val auditoriumCapacity: String
        val trainerMax: String
        val attendDay: String
        val attendTime: String
        val status: JournalSectionInfo.Status
        var denialNotice = ""

        doc.selectFirst("#main").apply {
            type = selectFirst("> div > table > tbody > tr:nth-child(1) > td:nth-child(2)").text() ?: "N/A"
            time = selectFirst("> div > table > tbody > tr:nth-child(2) > td:nth-child(2)").text() ?: "N/A"
            trainer = selectFirst("> div > table > tbody > tr:nth-child(3) > td:nth-child(2)").text() ?: "N/A"
            auditorium = selectFirst("> div > table > tbody > tr:nth-child(4) > td:nth-child(2)").text() ?: "N/A"
        }
        doc.selectFirst("#lesson").apply {
            groups = selectFirst("> div > table > tbody > tr:nth-child(4) > td:nth-child(2)").text() ?: "N/A"
            faculty = selectFirst("> div > table > tbody > tr:nth-child(5) > td:nth-child(2)").text() ?: "N/A"
        }
        doc.selectFirst("#sect").apply {
            healthGroups = selectFirst("> div > table > tbody > tr:nth-child(1) > td:nth-child(2)").text() ?: "N/A"
            auditoriumCapacity = selectFirst("> div > table > tbody > tr:nth-child(2) > td:nth-child(2)").text() ?: "N/A"
            trainerMax = selectFirst("> div > table > tbody > tr:nth-child(3) > td:nth-child(2)").text() ?: "N/A"
            attendDay = selectFirst("> div > table > tbody > tr:nth-child(4) > td:nth-child(2)").text() ?: "N/A"
            attendTime = selectFirst("> div > table > tbody > tr:nth-child(5) > td:nth-child(2)").text() ?: "N/A"

            status = selectFirst(".text-center")?.run {
                denialNotice = text()
                JournalSectionInfo.Status.NotAvailable
            } ?: selectFirst("button")?.run {
                if (text().contains("Отменить")) {
                    JournalSectionInfo.Status.Booked
                } else {
                    JournalSectionInfo.Status.Available
                }
            } ?: JournalSectionInfo.Status.NotAvailable
        }
        doc.selectFirst("#teacher").apply {
            trainerUrl = selectFirst("> div > table > tbody > tr:nth-child(1) > td:nth-child(2)")?.attr("href") ?: "N/A"
        }

        return JournalSectionInfo(
            type = type,
            time = time,
            trainer = trainer,
            trainerUrl = trainerUrl,
            auditorium = auditorium,
            groups = groups,
            faculty = faculty,
            healthGroups = healthGroups,
            auditoriumCapacity = auditoriumCapacity,
            trainerMax = trainerMax,
            attendDay = attendDay,
            attendTime = attendTime,
            status = status,
            denialNotice = denialNotice,
        )
    }
}