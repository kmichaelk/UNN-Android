package io.github.kmichaelk.unnandroid.models.journal

import java.util.Date

data class JournalSectionTimetable(
    val date: Date,
    val sections: Map<String, List<JournalSection>> // <Time, JournalSection>
)