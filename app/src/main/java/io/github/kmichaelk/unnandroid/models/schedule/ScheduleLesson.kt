package io.github.kmichaelk.unnandroid.models.schedule

import com.google.gson.annotations.SerializedName
import java.util.Date

data class ScheduleLesson(
    @SerializedName("discipline")
    val discipline: String,

    @SerializedName("kindOfWork")
    val kind: String?,

    @SerializedName("kindOfWorkOid")
    val kindId: Int,

    @SerializedName("lecturer")
    var lecturer: String?,

    @SerializedName("lecturer_rank")
    var lecturerRank: String?,

    @SerializedName("lessonNumberStart")
    val number: Int,

    @SerializedName("beginLesson")
    val begin: String,

    @SerializedName("endLesson")
    val end: String,

    @SerializedName("auditorium")
    val auditorium: String,

    @SerializedName("building")
    val building: String,

    @SerializedName("stream")
    val stream: String?,

    @SerializedName("date")
    val date: Date
)