package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kmichaelk.unnandroid.models.portal.PortalPaginatedResults
import io.github.kmichaelk.unnandroid.ui.state.UiError
import io.github.kmichaelk.unnandroid.ui.state.UserSearchScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException

@OptIn(FlowPreview::class)
abstract class UserSearchScreenViewModel<T> : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<PortalPaginatedResults<T>?>(null)
    val searchResults = _searchResults.asStateFlow()

    private val _uiState = MutableStateFlow(UserSearchScreenState<T>())
    val uiState = _uiState.asStateFlow()

    private var loadMoreJob: Job? = null

    fun load() {
        _uiState.update { it.copy(error = null) }
        viewModelScope.launch {
            _searchQuery.debounce(300L).collect { query ->
                _uiState.update { it.copy(isSearching = true, offset = 0) }

                loadMoreJob?.cancel()
                viewModelScope.async(Dispatchers.IO) {
                    var result: PortalPaginatedResults<T>? = null
                    try {
                        result = fetch(query, 0, _uiState.value.perPage)
                    } catch (ex: Exception) {
                        Timber.e(ex)
                        _uiState.update { it.copy(error = UiError.from(ex)) }
                    }
                    _uiState.update { it.copy(query = query) }
                    _searchResults.update { result }
                }.await()

                _uiState.update { it.copy(isSearching = false) }
            }
        }
    }

    fun loadMore() {
        assert(searchResults.value != null)

        _uiState.update { it.copy(error = null) }

        loadMoreJob?.cancel()
        loadMoreJob = viewModelScope.launch(Dispatchers.IO) {
            val off = _uiState.value.offset + _uiState.value.perPage
            try {
                val result = fetch(_uiState.value.query, off, _uiState.value.perPage)
                _searchResults.update { result.copy(
                    items = _searchResults.value!!.items.toMutableList().apply {
                        addAll(result.items)
                    }
                ) }
                _uiState.update { it.copy(offset = off) }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update { it.copy(
                    error = UiError.from(ex),
                ) }
            }
        }
    }

    fun onSearchQueryChange(text: String) {
        _searchQuery.value = text
    }

    abstract suspend fun fetch(query: String, offset: Int, take: Int) : PortalPaginatedResults<T>
}