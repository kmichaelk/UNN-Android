package io.github.kmichaelk.unnandroid.converters.journal

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class JournalSectionFilterConstraintsConverter : Converter<ResponseBody, JournalSectionFilterConstraints> {

    override fun convert(value: ResponseBody): JournalSectionFilterConstraints = value.use { body ->
        Jsoup.parse(body.string()).run {
            JournalSectionFilterConstraints(
                types = select("#section option").map { it.text() },
                trainers = select("#lector option").map { it.text() },
                buildings = select("#zd option").map { it.text() },
            )
        }
    }
}