package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalAvatarEx(

    @SerializedName("orig")
    val urlOriginal: String?,

    @SerializedName("small")
    val urlSmall: String?,

    @SerializedName("thumbnail")
    val urlThumbnail: String?,
) {

    fun isAvailable() = urlOriginal != null
}
