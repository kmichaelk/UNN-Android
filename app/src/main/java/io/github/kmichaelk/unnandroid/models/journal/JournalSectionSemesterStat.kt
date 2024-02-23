package io.github.kmichaelk.unnandroid.models.journal

data class JournalSectionSemesterStat(
    val ord: Int,
    //
    val total: Int,
    val left: Int,
    //
    val attendedTotal: Int,
    val attendedSections: Int,
    val attendedScheduled: Int,
    //
    val missed: Int,
    //
    val history: List<JournalSectionRecord>,
)
