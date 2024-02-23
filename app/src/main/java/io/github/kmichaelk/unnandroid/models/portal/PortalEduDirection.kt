package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalEduDirection(

    @SerializedName("id")
    val id: Int,

    @SerializedName("title")
    val title: String,
)
