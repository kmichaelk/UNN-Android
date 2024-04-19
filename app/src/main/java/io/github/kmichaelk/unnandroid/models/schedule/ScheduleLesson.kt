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

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ScheduleLesson(
    @SerializedName("discipline")
    val discipline: String,

    @SerializedName("kindOfWork")
    val kind: String?,

    @SerializedName("kindOfWorkOid")
    val kindId: Int,

    @SerializedName("lecturer")
    var lecturer: String?,

    @SerializedName("lecturer_rank")
    var lecturerRank: String?,

    @SerializedName("lessonNumberStart")
    val number: Int,

    @SerializedName("beginLesson")
    val begin: String,

    @SerializedName("endLesson")
    val end: String,

    @SerializedName("auditorium")
    val auditorium: String,

    @SerializedName("building")
    val building: String,

    @SerializedName("stream")
    val stream: String?,

    @SerializedName("date")
    val date: Date
)