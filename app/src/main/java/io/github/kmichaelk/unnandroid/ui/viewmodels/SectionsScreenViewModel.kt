/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

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