package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.api.auth.PortalAuthInterceptor
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.network.HostScopedInterceptor
import io.github.kmichaelk.unnandroid.ui.state.FeedScreenState
import io.github.kmichaelk.unnandroid.ui.state.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : BaseFeedViewModel(portalClient) {

    private val _uiState = MutableStateFlow(FeedScreenState())
    val uiState = _uiState.asStateFlow()

    private var lastPostIds: List<Int> = emptyList()
    private var loadJob: Job? = null

    // todo: extract timestamp
    private suspend fun fetch(page: Int): List<PortalFeedPost> =
        portalClient.getFeed(page, lastPostIds, 0).apply {
            lastPostIds = map { it.id }
        }

    fun load() = viewModelScope.launch(Dispatchers.IO) {
        val page = 1
        lastPostIds = emptyList()
        _uiState.update { it.copy(error = null, page = page) }
        try {
            val feed = fetch(page)
            _uiState.update { it.copy(
                data = feed,
            ) }
        } catch (ex: Exception) {
            Timber.e(ex)
            _uiState.update { it.copy(
                error = UiError.from(ex),
            ) }
        }
    }

    fun loadMore() {
        assert(_uiState.value.data != null)

        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = fetch(_uiState.value.page + 1)
                _uiState.update { it.copy(
                    data = _uiState.value.data!!.toMutableList().apply { addAll(result) },
                    page = it.page + 1
                ) }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update { it.copy(
                    error = UiError.from(ex),
                ) }
            }
        }
    }
}