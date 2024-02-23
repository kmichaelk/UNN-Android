package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PortalUser (

    @SerializedName("id")
    override val id: Int,

    @SerializedName("fullname")
    override val name: String,

    @SerializedName("photo")
    override val avatar: PortalAvatarEx,

    @SerializedName("profiles")
    val profiles: List<PortalProfile>,

    @SerializedName("last_activity_date")
    val lastActivity: Date?,

) : PortalPerson
