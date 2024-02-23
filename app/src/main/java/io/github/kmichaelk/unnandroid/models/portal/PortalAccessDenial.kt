package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalAccessDenial(

    @SerializedName("error")
    val error: String,

    @SerializedName("error_description")
    val errorDescription: String,

    @SerializedName("sessid")
    val sessionId: String,
)
