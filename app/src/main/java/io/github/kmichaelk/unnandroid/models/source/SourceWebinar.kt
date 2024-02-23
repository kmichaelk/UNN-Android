package io.github.kmichaelk.unnandroid.models.source

import com.google.gson.annotations.SerializedName
import java.util.Date

data class SourceWebinar(

    @SerializedName("title")
    val title: String,

    @SerializedName("discipline")
    val discipline: String,

    @SerializedName("comment")
    val comment: String,

    @SerializedName("url_record")
    val urlRecord: String,

    @SerializedName("url_stream")
    val urlStream: String,

    @SerializedName("date")
    val dateStr: String,

    @SerializedName("time")
    val timeStr: String,

    var parsedDate: Date = Date(),

    @SerializedName("login")
    val uploadedBy: String,

)
