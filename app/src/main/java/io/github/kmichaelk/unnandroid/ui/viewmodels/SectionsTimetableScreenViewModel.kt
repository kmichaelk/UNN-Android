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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.JournalClient
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionTimetable
import io.github.kmichaelk.unnandroid.ui.state.SectionsTimetableScreenState
import io.github.kmichaelk.unnandroid.ui.state.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SectionsTimetableScreenViewModel @Inject constructor(
    private val journalClient: JournalClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(SectionsTimetableScreenState())
    val uiState = _uiState.asStateFlow()

    private var job: Job? = null

    fun load(): Job {
        val date = _uiState.value.date
        _uiState.update { it.copy(error = null) }
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_uiState.value.filterConstraints == null) {
                    val constraints = journalClient.getSectionsFilterConstraints(date)
                    _uiState.update { it.copy(
                        filterConstraints = constraints
                    ) }
                }
                val timetable = journalClient.getSectionsTimetable(date)
                if (date != _uiState.value.date) return@launch
                _uiState.update { it.copy(
                    data = timetable
                ) }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update { it.copy(error = UiError.from(ex)) }
            }
        }
        return job!!
    }

    fun setDate(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun updateFilters(updater: (SectionsTimetableScreenState.Filters) -> SectionsTimetableScreenState.Filters) =
        _uiState.update { it.copy(filters = updater(it.filters)) }

    fun applyFilters(): JournalSectionTimetable {
        assert(_uiState.value.data != null)
        val data = _uiState.value.data!!

        val filters = _uiState.value.filters
        if (filters.none()) return data

        return data.copy(sections = _uiState.value.data?.sections!!.toMutableMap().mapValues { entry ->
            entry.value.toMutableList().filter {
                (filters.type == null || filters.type == it.type)
                        && (filters.trainer == null || filters.trainer == it.trainer)
                        && (filters.building == null || it.auditorium.contains(filters.building))
            }
        }.filter { it.value.isNotEmpty() })
    }
}