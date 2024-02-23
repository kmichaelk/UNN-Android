package io.github.kmichaelk.unnandroid.converters

import com.google.gson.reflect.TypeToken
import io.github.kmichaelk.unnandroid.converters.source.SourceSemesterListConverter
import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class SourceServiceConverterFactory private constructor(): Converter.Factory() {

    companion object {
        fun create() = SourceServiceConverterFactory()
    }

    private val semesterListType: Type = TypeToken.getParameterized(List::class.java, SourceSemester::class.java).type
    private val semesterListConverter = SourceSemesterListConverter()

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? = when (type) {
        semesterListType -> semesterListConverter
        else -> null
    }
}