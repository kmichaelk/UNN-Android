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