package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import io.github.kmichaelk.unnandroid.models.source.SourceSemester

data class SectionsScreenState(
    val semester: JournalSectionSemesterStat? = null,
    val semesters: List<JournalSectionSemesterStat>? = null
)
