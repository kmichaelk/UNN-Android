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

package io.github.kmichaelk.unnandroid.models.schedule

import java.util.Calendar
import java.util.Date

data class ScheduleDateRange(
    val begin: Date,
    val end: Date
) {
    companion object {
        fun thisWeek(): ScheduleDateRange {
            val calendar = Calendar.getInstance()
            val begin = calendar.time
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val end = calendar.time

            return ScheduleDateRange(begin, end)
        }
    }
}