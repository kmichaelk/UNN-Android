package io.github.kmichaelk.unnandroid.models.journal

import java.util.Date

data class JournalSectionRecord(
    val date: Date,
    val timespan: String,
    val type: String,
    val trainer: String,
    val auditorium: String,
    val mark: Mark,
) {
    enum class Mark { Attended, Missed, NotSet }
}