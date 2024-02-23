package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PortalOrder(

    @SerializedName("id")
    val id: Int,

    @SerializedName("number")
    val number: String,

    @SerializedName("date")
    val date: Date,

    @SerializedName("date_approve")
    val dateApprove: Date,

    @SerializedName("title")
    val title: String,

    @SerializedName("action")
    val description: String,
)
