package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost

data class FeedScreenState(
    val data: List<PortalFeedPost>? = null,
    val error: UiError? = null,

    val page: Int = 1,
)