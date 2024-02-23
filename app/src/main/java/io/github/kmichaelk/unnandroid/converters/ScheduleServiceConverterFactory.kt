package io.github.kmichaelk.unnandroid.converters

import io.github.kmichaelk.unnandroid.converters.schedule.ScheduleQueryConverter
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleScope
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.Date

class ScheduleServiceConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create() = ScheduleServiceConverterFactory()
    }

    private val queryConverter = ScheduleQueryConverter<Any>()

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String>? = when (type) {
        Date::class.java, ScheduleScope::class.java -> queryConverter
        else -> null
    }
}