package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalResponseWrapper<T>(

    @SerializedName("result")
    val result: T?,

    @SerializedName("error")
    val error: String?,

    @SerializedName("error_description")
    val errorDescription: String?,

    @SerializedName("sessid")
    val sessionId: String?,
)