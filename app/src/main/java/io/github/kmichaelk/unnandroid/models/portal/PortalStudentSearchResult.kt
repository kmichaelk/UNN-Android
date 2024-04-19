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

data class PortalStudentSearchResult(

    @SerializedName("id")
    val recordId: Int,

    @SerializedName("user_id")
    override val id: Int,

    @SerializedName("fullname")
    override val name: String,

    @SerializedName("department")
    val department: String,

    @SerializedName("department_id")
    val departmentId: Int,

    @SerializedName("edu_direction")
    val direction: String,

    @SerializedName("edu_direction_id")
    val directionId: Int,

    @SerializedName("edu_form")
    val educationForm: String,

    @SerializedName("edu_level")
    val educationLevel: String,

    @SerializedName("edu_group")
    val group: String,

    @SerializedName("edu_group_id")
    val groupId: Int,

    @SerializedName("edu_course")
    val course: Int,

    @SerializedName("photo")
    override val avatar: PortalAvatarEx

) : PortalPerson
