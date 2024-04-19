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

package io.github.kmichaelk.unnandroid.api

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionTimetable
import java.util.Date

interface JournalClient {

    suspend fun getSectionsStats() : List<JournalSectionSemesterStat>
    suspend fun getSectionsFilterConstraints(date: Date) : JournalSectionFilterConstraints
    suspend fun getSectionsTimetable(date: Date) : JournalSectionTimetable
    suspend fun getSectionInfo(id: String) : JournalSectionInfo
    suspend fun setSectionStatus(id: String, status: Boolean) : Boolean

}