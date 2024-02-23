package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.JournalClient
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionRecord
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import io.github.kmichaelk.unnandroid.ui.state.SectionsScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SectionsScreenViewModel @Inject constructor(
    private val journalClient: JournalClient
) : StaticDataScreenViewModel<List<JournalSectionRecord>>() {

    private val _sUiState = MutableStateFlow(SectionsScreenState())
    val sUiState = _sUiState.asStateFlow()

    fun setSemester(semester: JournalSectionSemesterStat) {
        _sUiState.update { it.copy(
            semester = semester
        ) }
        _uiState.update { it.copy(
            data = semester.history
        ) }
    }

    override suspend fun fetch(): List<JournalSectionRecord> {
        val semesters = journalClient.getSectionsStats()
        val current = semesters[semesters.size - 1]
        _sUiState.update { it.copy(
            semester = current,
            semesters = semesters
        ) }
        return current.history
    }
}