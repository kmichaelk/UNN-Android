package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat

data class SectionsScreenState(
    val semester: JournalSectionSemesterStat? = null,
    val semesters: List<JournalSectionSemesterStat>? = null
)
