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

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
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
                data = feed
            ) }
        } catch (ex: Exception) {
            Timber.e(ex)
            _uiState.update { it.copy(
                error = UiError.from(ex)
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