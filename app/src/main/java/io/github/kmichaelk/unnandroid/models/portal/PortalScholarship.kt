package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PortalScholarship(

    @SerializedName("id")
    val id: Int,

    @SerializedName("type")
    val type: String,

    @SerializedName("date_begin")
    val dateBegin: Date,

    @SerializedName("date_end")
    val dateEnd: Date,

    @SerializedName("sum")
    val amount: Int,

    @SerializedName("order")
    val order: String,
)
