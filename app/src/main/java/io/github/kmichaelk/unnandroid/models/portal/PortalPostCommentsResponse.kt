package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalPostCommentsResponse(
    @SerializedName("messageList")
    val messageList: String
)
