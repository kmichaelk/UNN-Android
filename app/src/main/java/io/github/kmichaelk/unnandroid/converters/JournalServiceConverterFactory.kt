package io.github.kmichaelk.unnandroid.converters

import com.google.gson.reflect.TypeToken
import io.github.kmichaelk.unnandroid.converters.journal.JournalDateConverter
import io.github.kmichaelk.unnandroid.converters.journal.JournalSectionFilterConstraintsConverter
import io.github.kmichaelk.unnandroid.converters.journal.JournalSectionInfoConverter
import io.github.kmichaelk.unnandroid.converters.journal.JournalSectionStatListConverter
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.Date

class JournalServiceConverterFactory private constructor(): Converter.Factory() {

    companion object {
        fun create() = JournalServiceConverterFactory()
    }

    private val sectionStatListType: Type = TypeToken.getParameterized(List::class.java, JournalSectionSemesterStat::class.java).type

    private val sectionStatListTypeConverter = JournalSectionStatListConverter()
    private val sectionFilterConstraintsConverter = JournalSectionFilterConstraintsConverter()
    private val sectionInfoConverter = JournalSectionInfoConverter()
    private val dateTypeConverter = JournalDateConverter<Any>()

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? = when (type) {
        Date::class.java -> dateTypeConverter
        else -> null
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? = when (type) {
        sectionStatListType -> sectionStatListTypeConverter
        JournalSectionInfo::class.java -> sectionInfoConverter
        JournalSectionFilterConstraints::class.java -> sectionFilterConstraintsConverter
        else -> null
    }
}