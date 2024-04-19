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

import android.annotation.SuppressLint
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionRecord
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import java.text.SimpleDateFormat
import java.util.regex.Pattern

class JournalSectionStatListConverter : Converter<ResponseBody, List<JournalSectionSemesterStat>> {

    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd")

    private val attendedPattern: Pattern = Pattern.compile("(.*?) Секция (.*?) Физкультура (.*?)")

    override fun convert(value: ResponseBody): List<JournalSectionSemesterStat> = value.use { body ->
        Jsoup.parse(body.string())
            .body()
            .select(".tab-pane").map {
                val semesterOrd = Integer.parseInt(it.id().substring(3)) //

                val total = Integer.parseInt(
                    it.selectFirst("> div.row > div:nth-child(1) > div > div.card-body > div > div.ml-auto > h2 > span")!!
                        .text()
                )
                val missed = Integer.parseInt(
                    it.selectFirst("> div.row > div:nth-child(3) > div > div.card-body > div > div.ml-auto > h2 > span")!!
                        .text()
                )
                val left = Integer.parseInt(
                    it.selectFirst("> div.row > div:nth-child(4) > div > div.card-body > div > div.ml-auto > h2 > span")!!
                        .text()
                )

                val attendedTotal: Int
                var attendedSections: Int = -1
                var attendedScheduled: Int = -1
                //
                val attendedTotalRaw =
                    it.selectFirst("> div.row > div:nth-child(2) > div > div.card-body > div > div.ml-auto > h2 > span")!!
                        .text()
                if (attendedTotalRaw.contains("Физкультура")) {
                    val matcher = attendedPattern.matcher(attendedTotalRaw)
                    if (!matcher.matches()) {
                        throw RuntimeException("Malformed JournalUnn response")
                    }
                    attendedTotal = Integer.parseInt(matcher.group(1)!!)
                    attendedSections = Integer.parseInt(matcher.group(2)!!)
                    attendedScheduled = Integer.parseInt(matcher.group(3)!!)
                } else {
                    attendedTotal = Integer.parseInt(attendedTotalRaw)
                }

                val history = it.select("tbody tr").map { record ->
                    val td = record.select("td").map { it.text() }

                    val time = td[0].split(" ")
                    val title = td[1]
                    val trainer = td[2]
                    val auditorium = td[3]
                    val mark = when (td[4]) {
                        "V" -> JournalSectionRecord.Mark.Attended
                        "Н" -> JournalSectionRecord.Mark.Missed
                        else -> JournalSectionRecord.Mark.NotSet
                    }

                    JournalSectionRecord(
                        date = dateFormat.parse(time[0])!!,
                        timespan = time[1],
                        type = title,
                        trainer = trainer,
                        auditorium = auditorium,
                        mark = mark
                    )
                }

                JournalSectionSemesterStat(
                    ord = semesterOrd,

                    total = total,
                    left = left,

                    attendedTotal = attendedTotal,
                    attendedSections = attendedSections,
                    attendedScheduled = attendedScheduled,

                    missed = missed,

                    history = history,
                )
            }
    }
}