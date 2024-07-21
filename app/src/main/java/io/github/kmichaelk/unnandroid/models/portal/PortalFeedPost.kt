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

package io.github.kmichaelk.unnandroid.models.portal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PortalFeedPost(
    override val id: Int,
    val entityXmlId: String,
    val author: PortalFeedUser,
    val datetime: String,

    val html: String,

    val attachments: List<String>,
    val files: List<PortalFeedAttachedFile>,

    val recipients: List<PortalFeedPostRecipient>,
    override val reactions: Map<PortalFeedReaction, Int>,
    val voteKeyPart: String?,

    val commentsCount: Int,
    val views: Int,

    val url: String,
) : PortalFeedVoteable, Parcelable {
    override fun getVoteKey() = "BLOG_POST-${id}.${voteKeyPart}"
    override fun getEntityType() = PortalFeedEntityType.Post
}
