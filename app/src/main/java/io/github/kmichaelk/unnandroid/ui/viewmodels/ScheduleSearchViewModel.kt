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