package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
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

    private val _uiState = MutableStateFlow(StaticDataScreenState<List<PortalFeedComment>>())
    val uiState = _uiState.asStateFlow()

    fun load(post: PortalFeedPost): Job {
        _uiState.update { it.copy(error = null) }
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                val comments = portalClient.getPostComments(post.entityXmlId)
                _uiState.update { it.copy(
                    data = comments,
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