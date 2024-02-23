package io.github.kmichaelk.unnandroid.models.journal

import com.google.gson.annotations.SerializedName

data class JournalSectionInfoRaw(

    @SerializedName("resourceId")
    val trainer: String,

    @SerializedName("title")
    val type: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("tooltip")
    val dataEx: String,

)
