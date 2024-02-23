package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalDepartment(

    @SerializedName("department_id")
    val id: Int,

    @SerializedName("title")
    val title: String,

    @SerializedName("child")
    val child: PortalDepartment?,
)