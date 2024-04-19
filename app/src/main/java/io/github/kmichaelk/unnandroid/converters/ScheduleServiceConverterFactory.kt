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