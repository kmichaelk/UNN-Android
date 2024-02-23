package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PortalMarks(

    @SerializedName("year")
    val year: Int,

    @SerializedName("semesters")
    val semesters: List<Semester>
) {

    data class Semester(

        @SerializedName("semester")
        val ord: Int,

        @SerializedName("data")
        val data: List<Entry>,
    )

    data class Entry(

        @SerializedName("subject")
        val subject: String,

        @SerializedName("control_type")
        val controlType: String,

        @SerializedName("mark")
        val mark: String,

        @SerializedName("mark_title")
        val markTitle: String,

        @SerializedName("lecturers")
        val lecturer: String?,

        @SerializedName("zet")
        val weight: Int,

        @SerializedName("hours")
        val hours: Int,

        @SerializedName("date")
        val date: Date,
    )
}
