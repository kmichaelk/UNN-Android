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

data class PortalEmployee (

    @SerializedName("id")
    override val id: Int,

    @SerializedName("fullname")
    override val name: String,

    @SerializedName("photo")
    override val avatar: PortalAvatarEx,

    @SerializedName("profiles")
    val profiles: List<PortalProfileC>

) : PortalPerson {

    data class PortalProfileC(

        @SerializedName("id")
        val id: Int,

        @SerializedName("department")
        val department: List<PortalDepartment>?,

        @SerializedName("faculty")
        val faculty: PortalFaculty?,

        @SerializedName("job_title")
        val jobTitle: String?,

        @SerializedName("job_type")
        val jobType: String?,

        @SerializedName("type")
        val type: PortalUserType,
    )

}