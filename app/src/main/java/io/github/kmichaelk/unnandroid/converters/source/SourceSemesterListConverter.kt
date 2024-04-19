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

package io.github.kmichaelk.unnandroid.converters.source

import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter
import java.util.regex.Pattern

class SourceSemesterListConverter : Converter<ResponseBody, List<SourceSemester>> {

    private val pattern: Pattern = Pattern.compile("(.*?)-(.*?) год, (.*?) семестр \\((.*?)\\)")

    override fun convert(value: ResponseBody): List<SourceSemester> = value.use { body ->
        Jsoup.parse(body.string())
            .select("#page > div:nth-child(3) > div:nth-child(1) > div > a").map {
                val internalIds = it.attr("href").split("/")
                val yearId = Integer.parseInt(internalIds[internalIds.size - 2])
                val ord = Integer.parseInt(internalIds[internalIds.size - 1])

                val text = it.text()
                val matcher = pattern.matcher(text)

                if (!matcher.matches()) {
                    throw RuntimeException("Malformed SourceUnn response")
                }

                SourceSemester(
                    yearBegin = Integer.parseInt(matcher.group(1)!!),
                    yearEnd = Integer.parseInt(matcher.group(2)!!),
                    yearId = yearId,
                    ord = ord,
                    season = if ("весна" == matcher.group(4)!!) SourceSemester.Season.Spring else SourceSemester.Season.Fall
                )
            }
    }
}