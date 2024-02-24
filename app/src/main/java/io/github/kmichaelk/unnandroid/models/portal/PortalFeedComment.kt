package io.github.kmichaelk.unnandroid.models.portal

data class PortalFeedComment(
    val id: Int,
    val author: PortalFeedUser,
    val datetime: String,
    val html: String,
    val attachmentUrl: String?,
)
