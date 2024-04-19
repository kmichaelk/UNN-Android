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

data class PortalProfile(

    @SerializedName("id")
    val id: Int,

    @SerializedName("department")
    val department: PortalDepartment?,

    @SerializedName("faculty")
    val faculty: PortalFaculty?,

    @SerializedName("job_title")
    val jobTitle: String?,

    @SerializedName("job_type")
    val jobType: String?,

    @SerializedName("manager")
    val manager: Manager?,

    @SerializedName("edu_specialization")
    val eduSpecialization: PortalEduSpecialization?,

    @SerializedName("edu_qualification")
    val eduQualification: PortalEduSpecialization?,

    @SerializedName("edu_direction")
    val eduDirection: PortalEduDirection?,

    @SerializedName("edu_group")
    val eduGroup: PortalEduGroup?,

    @SerializedName("edu_level")
    val eduLevel: String?,

    @SerializedName("edu_year")
    val eduYear: Int,

    @SerializedName("edu_course")
    val eduCourse: Int,

    @SerializedName("edu_status")
    val eduStatus: String?,

    @SerializedName("edu_form")
    val eduForm: String?,

    @SerializedName("work_address")
    val workAddress: String?,

    @SerializedName("type")
    val type: PortalUserType,
) {

    data class Manager(
        @SerializedName("id")
        val id: Int,

        @SerializedName("fullname")
        val name: String,
    )
}
