package io.github.kmichaelk.unnandroid.api

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionTimetable
import java.util.Date

interface JournalClient {

    suspend fun getSectionsStats() : List<JournalSectionSemesterStat>
    suspend fun getSectionsFilterConstraints(date: Date) : JournalSectionFilterConstraints
    suspend fun getSectionsTimetable(date: Date) : JournalSectionTimetable
    suspend fun getSectionInfo(id: String) : JournalSectionInfo
    suspend fun setSectionStatus(id: String, status: Boolean) : Boolean

}