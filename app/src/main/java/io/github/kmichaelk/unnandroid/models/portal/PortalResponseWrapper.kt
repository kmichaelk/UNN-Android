package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalResponseWrapper<T>(

    @SerializedName("result")
    val result: T
)