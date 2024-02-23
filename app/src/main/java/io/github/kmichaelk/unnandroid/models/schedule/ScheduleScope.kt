package io.github.kmichaelk.unnandroid.models.schedule

import com.google.gson.annotations.SerializedName

enum class ScheduleScope(val displayName: String) {

    @SerializedName("group")
    Group("Группа"),

    @SerializedName("person")
    Person("Персона"),

    @SerializedName("student")
    Student("Студент"),

    @SerializedName("lecturer")
    Lecturer("Преподаватель"),

    @SerializedName("auditorium")
    Auditorium("Аудитория")
}