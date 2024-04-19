package io.github.kmichaelk.unnandroid.api.extractors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.github.kmichaelk.unnandroid.models.journal.JournalSection
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfoRaw
import org.jsoup.Jsoup
import java.util.regex.Pattern

class JournalTimetableExtractor {

    private val gson = Gson()

    private val patternOnClick = Pattern.compile("getpopup\\((.*?)\\)")
    private val patternStyle = Pattern.compile("(.*?)background-color:(.*?);")

    fun extract(exRaw: String, stdRaw: String): List<JournalSection> {
        val listExRaw = exRaw.run {
            val markerBegin = "events: "
            val markerEnd = "],"
            substring(indexOf(markerBegin) + markerBegin.length).run {
                substring(0, indexOf(markerEnd) + markerEnd.length - 1)
            }
        }
        val listEx = gson.fromJson<List<JournalSectionInfoRaw>>(
            listExRaw,
            TypeToken.getParameterized(List::class.java, JournalSectionInfoRaw::class.java).type
        )

        val statuses = Jsoup.parse(stdRaw).body().select(".timetable-item > a").associate {
            val id = it.attr("onclick").run {
                val matcher = patternOnClick.matcher(this)
                if (!matcher.matches()) {
                    throw RuntimeException("Malformed Journal Section - ID")
                }
                Integer.parseInt(matcher.group(1)!!)
            }
            val status = it.attr("style").run {
                val matcher = patternStyle.matcher(this)
                if (!matcher.matches()) {
                    throw RuntimeException("Malformed Journal Section - Status")
                }
                when (matcher.group(2)!!) {
                    "#B0E0E6" -> JournalSection.Status.Available
                    "#FFE4C4" -> JournalSection.Status.NotAvailable
                    "#e6f5d7" -> JournalSection.Status.Booked
                    else -> throw RuntimeException("Malformed Journal Section - Unsupported Status")
                }
            }
            id to status
        }

        return listEx.map {
            val data = it.dataEx.split("<br>")
            JournalSection(
                id = it.id,
                type = it.type,
                trainer = it.trainer,
                auditorium = data[2],
                timespan = data[3],
                status = statuses[it.id]!!
            )
        }
    }
}