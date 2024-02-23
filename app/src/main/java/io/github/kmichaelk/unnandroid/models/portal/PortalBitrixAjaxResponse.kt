package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalBitrixAjaxResponse<T>(

    @SerializedName("data")
    val data: T,

    //@SerializedName("errors")
    //val errors: String[],

    @SerializedName("status")
    val status: String,
)
