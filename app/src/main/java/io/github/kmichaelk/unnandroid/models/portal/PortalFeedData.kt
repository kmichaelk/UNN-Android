package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalFeedData(
    @SerializedName("html")
    val html: String
)