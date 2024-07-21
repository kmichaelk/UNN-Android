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
import io.github.kmichaelk.unnandroid.models.portal.PortalCommentsPage
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.ui.state.PostScreenState
import io.github.kmichaelk.unnandroid.ui.state.StaticDataScreenState
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
class PostScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : BaseFeedViewModel(portalClient) {

    private val _uiState = MutableStateFlow(PostScreenState())
    val uiState = _uiState.asStateFlow()

    private var entityXmlId: String = ""
    private var loadJob: Job? = null

    private suspend fun fetch(page: Int) = portalClient.getPostComments(
        entityXmlId = entityXmlId,
        pageNumber = _uiState.value.totalPages - page + 1,
    )

    fun load(post: PortalFeedPost): Job {
        entityXmlId = post.entityXmlId
        val page = 1

        _uiState.update {
            it.copy(
                error = null,
                page = page,
                totalPages = (post.commentsCount / 20) + 1
            )
        }
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.update {
                    it.copy(
                        data = fetch(page).comments,
                    )
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update {
                    it.copy(
                        error = UiError.from(ex),
                    )
                }
            }
        }
    }

    fun loadMore() {
        assert(_uiState.value.data != null)

        loadJob?.cancel()
        loadJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = fetch(_uiState.value.page + 1)
                _uiState.update {
                    it.copy(
                        data = _uiState.value.data!!.toMutableList().apply { addAll(result.comments) },
                        page = it.page + 1
                    )
                }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update {
                    it.copy(
                        error = UiError.from(ex),
                    )
                }
            }
        }
    }
}