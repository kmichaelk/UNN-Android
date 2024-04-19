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