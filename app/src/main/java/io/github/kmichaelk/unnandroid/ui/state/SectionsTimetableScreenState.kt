package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionTimetable
import java.util.Calendar
import java.util.Date

data class SectionsTimetableScreenState(
    val date: Date = Calendar.getInstance().run {
        add(Calendar.DATE, 1)
        time
    },

    val filterConstraints: JournalSectionFilterConstraints? = null,
    val data: JournalSectionTimetable? = null,

    val filters: Filters = Filters(),

    val error: UiError? = null
) {
    data class Filters(
        val type: String? = null,
        val trainer: String? = null,
        val building: String? = null,
    ) {
        fun none() = type == null && trainer == null && building == null
    }
}