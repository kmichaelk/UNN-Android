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