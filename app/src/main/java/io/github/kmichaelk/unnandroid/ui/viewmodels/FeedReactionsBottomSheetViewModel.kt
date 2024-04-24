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
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedReaction
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedVoteable
import io.github.kmichaelk.unnandroid.ui.state.FeedReactionsBottomSheetState
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
class FeedReactionsBottomSheetViewModel @Inject constructor(
    private val portalClient: PortalClient
): ViewModel() {

    private val _uiState = MutableStateFlow(FeedReactionsBottomSheetState())
    val uiState = _uiState.asStateFlow()

    private var loadMoreJob: Job? = null

    fun setEntity(entity: PortalFeedVoteable) {
        loadMoreJob?.cancel()
        _uiState.update { it.copy(
            entity = entity,
            data = entity.reactions.entries.associate { entry ->
                entry.key to FeedReactionsBottomSheetState.ReactionData(
                    total = entry.value
                )
            },
            error = null,
        ) }
    }

    fun loadMore(reaction: PortalFeedReaction) {
        assert(_uiState.value.entity != null)

        val entity = _uiState.value.entity!!
        val page = _uiState.value.data[reaction]!!.page + 1

        _uiState.update { it.copy(error = null) }

        loadMoreJob?.cancel()
        loadMoreJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = portalClient.getReactions(
                    entity = entity,
                    pageNumber = page,
                    reaction = reaction,
                )
                _uiState.update { it.copy(
                    data = it.data.toMutableMap().apply {
                        this[reaction] = this[reaction]!!.copy(
                            page = page,
                            complete = result.isEmpty(),
                            users = this[reaction]!!.users.toMutableList().apply {
                                addAll(result)
                            }
                        )
                    }
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