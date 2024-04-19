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
import io.github.kmichaelk.unnandroid.api.ScheduleClient
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.ui.state.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ScheduleSearchViewModel @Inject constructor(
    private val scheduleClient: ScheduleClient
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _error = MutableStateFlow<UiError?>(null)
    val error = _error.asStateFlow()

    private val _currentQuery = MutableStateFlow(_searchQuery.value)
    val currentQuery = _currentQuery.asStateFlow()

    private val emptySearchResults = emptyList<ScheduleEntity>()
    val searchResults = searchQuery
        .debounce(300L)
        .onEach {
            _isSearching.update { true }
            _error.update { null }
        }
        .map { query ->
            if (query.isNotBlank())
                viewModelScope.async(Dispatchers.IO) {
                    var result = emptySearchResults
                    try {
                        result = scheduleClient.searchEntity(query)
                    } catch (ex: Exception) {
                        Timber.e("Schedule search failed", ex)
                        _error.update { UiError.from(ex) }
                    }
                    _currentQuery.update { query }
                    result
                }.await()
            else emptySearchResults
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptySearchResults
        )

    fun onSearchQueryChange(text: String) {
        _searchQuery.value = text
    }
}