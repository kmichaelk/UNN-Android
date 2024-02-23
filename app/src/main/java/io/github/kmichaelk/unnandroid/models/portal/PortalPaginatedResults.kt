package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalPaginatedResults<T>(

    @SerializedName("all")
    val all: Int,

    @SerializedName("total")
    val total: Int,

    @SerializedName("items")
    val items: List<T>,
)
