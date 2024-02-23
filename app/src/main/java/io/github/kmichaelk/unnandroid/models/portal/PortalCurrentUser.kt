package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalCurrentUser(

    @SerializedName("ID")
    val id: Int,

    @SerializedName("NAME")
    val name: String,

    @SerializedName("LAST_NAME")
    val lastName: String,

    @SerializedName("SECOND_NAME")
    val secondName: String,

    @SerializedName("EMAIL")
    val email: String,

    @SerializedName("WORK_POSITION")
    val position: String,

    @SerializedName("WORK_NOTES")
    val notes: String,

    @SerializedName("PERSONAL_PHOTO")
    val avatarUrl: String?,
)
