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

data class PortalCurrentUser(

    @SerializedName("ID")
    val id: Int,

    @SerializedName("NAME")
    val name: String,

    @SerializedName("LAST_NAME")
    val lastName: String,

    @SerializedName("SECOND_NAME")
    val secondName: String,

    @SerializedName("EMAIL")
    val email: String,

    @SerializedName("WORK_POSITION")
    val position: String,

    @SerializedName("WORK_NOTES")
    val notes: String,

    @SerializedName("PERSONAL_PHOTO")
    val avatarUrl: String?,
)
