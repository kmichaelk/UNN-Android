package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalSearchRequestBody(

    @SerializedName("first")
    val first: Int,

    @SerializedName("rows")
    val rows: Int,

    @SerializedName("sortField")
    val sortField: String,

    @SerializedName("sortOrder")
    val sortOrder: Int,

    @SerializedName("filters")
    val filters: Map<String, Filter>,

    @SerializedName("globalFilter")
    val globalFilter: String,
) {

    data class Filter(

        @SerializedName("value")
        val value: String,

        @SerializedName("matchMode")
        val matchMode: String,

    )
}
