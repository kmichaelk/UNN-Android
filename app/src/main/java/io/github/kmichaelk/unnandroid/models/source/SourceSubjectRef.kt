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

package io.github.kmichaelk.unnandroid.models.sourceS

import com.google.gson.annotations.SerializedName
import io.github.kmichaelk.unnandroid.api.service.SourceService
import java.util.Date

sealed class SourceSubjectRef {

    data class File(

        @SerializedName("file_src_name")
        val filename: String,

        @SerializedName("file_hash")
        val hash: String,

        @SerializedName("file_size")
        val fileSize: Long,

        @SerializedName("comment")
        val comment: String,

        @SerializedName("file_date")
        val date: Date,

        @SerializedName("login")
        val uploadedBy: String,

    ) : SourceSubjectRef() {
        fun getDownloadUrl() = "${SourceService.BASE_URL}files/file.php?hash=${hash}"
    }

    data class Link(

        @SerializedName("link")
        val link: String,

        @SerializedName("comment")
        val comment: String,

        @SerializedName("datetime")
        val date: Date,

        @SerializedName("login")
        val uploadedBy: String,

    ) : SourceSubjectRef()

}