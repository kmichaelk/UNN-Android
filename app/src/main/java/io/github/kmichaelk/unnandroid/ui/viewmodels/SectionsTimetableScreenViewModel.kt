package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.JournalClient
import io.github.kmichaelk.unnandroid.models.journal.JournalSection
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

    fun load(): Job {
        _uiState.update { it.copy(error = null) }
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                if (_uiState.value.filterConstraints == null) {
                    val constraints = journalClient.getSectionsFilterConstraints(_uiState.value.date)
                    _uiState.update { it.copy(
                        filterConstraints = constraints
                    ) }
                }
                val timetable = journalClient.getSectionsTimetable(_uiState.value.date)
                _uiState.update { it.copy(
                    data = timetable
                ) }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update { it.copy(error = UiError.from(ex)) }
            }
        }
    }

    fun setDate(date: Date): Boolean {
        if (_uiState.value.date == date) {
            return false
        }
        _uiState.update { it.copy(date = date) }
        return true
    }

    fun updateFilters(updater: (SectionsTimetableScreenState.Filters) -> SectionsTimetableScreenState.Filters) =
        _uiState.update { it.copy(filters = updater(it.filters)) }

    fun filteredSections(): Map<String, List<JournalSection>> {
        assert(_uiState.value.data?.sections != null)

        val filters = _uiState.value.filters
        if (filters.none()) return _uiState.value.data?.sections!!

        return _uiState.value.data?.sections!!.toMutableMap().mapValues { entry ->
            entry.value.toMutableList().filter {
                (filters.type == null || filters.type == it.type)
                        && (filters.trainer == null || filters.trainer == it.trainer)
                        && (filters.building == null || it.auditorium.contains(filters.building))
            }
        }.filter { it.value.isNotEmpty() }
    }
}