package io.github.kmichaelk.unnandroid.models.schedule

import com.google.gson.annotations.SerializedName

data class ScheduleEntity(
    @SerializedName("id") val id: String,
    @SerializedName("label") val label: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val scope: ScheduleScope
)
