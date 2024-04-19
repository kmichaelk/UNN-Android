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

package io.github.kmichaelk.unnandroid.ui.extensions

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.Color
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleLesson
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleScope
import io.github.kmichaelk.unnandroid.models.source.SourceSemester

fun ScheduleScope.icon() = when (this) {
    ScheduleScope.Group -> Icons.Default.Group
    ScheduleScope.Person -> Icons.Default.AccountCircle
    ScheduleScope.Student -> Icons.Default.Person
    ScheduleScope.Lecturer -> Icons.Default.School
    ScheduleScope.Auditorium -> Icons.Default.Apartment
}

fun ScheduleLesson.color() = when(kindId) {
    // Лекция
    262 -> Color(0xFFB3E185)
    // Лабораторная работа
    263, 334 -> Color(0xFF78D2FF)
    // Практика
    264, 335 -> Color(0xFFFFC26B)
    // Консультация
    271 -> Color(0xFFAE8FE9)
    // Экзамен
    266 -> Color(0xFFFF8080)
    // Зачет
    268 -> Color(0xFFFF80C7)
    //
    else -> Color(0xFFECE7EC)
}

fun SourceSemester.Season.displayName() = when (this) {
    SourceSemester.Season.Spring -> "весна"
    SourceSemester.Season.Fall -> "осень"
}