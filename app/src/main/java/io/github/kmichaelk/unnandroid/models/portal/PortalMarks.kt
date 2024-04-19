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

package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PortalMarks(

    @SerializedName("year")
    val year: Int,

    @SerializedName("semesters")
    val semesters: List<Semester>
) {

    data class Semester(

        @SerializedName("semester")
        val ord: Int,

        @SerializedName("data")
        val data: List<Entry>,
    )

    data class Entry(

        @SerializedName("subject")
        val subject: String,

        @SerializedName("control_type")
        val controlType: String,

        @SerializedName("mark")
        val mark: String,

        @SerializedName("mark_title")
        val markTitle: String,

        @SerializedName("lecturers")
        val lecturer: String?,

        @SerializedName("zet")
        val weight: Int,

        @SerializedName("hours")
        val hours: Int,

        @SerializedName("date")
        val date: Date,
    )
}
