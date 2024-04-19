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