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

package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.models.portal.PortalFeedReaction
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedVoteable
import io.github.kmichaelk.unnandroid.models.portal.PortalUserRecord

data class FeedReactionsBottomSheetState(
    val entity: PortalFeedVoteable? = null,
    val data: Map<PortalFeedReaction, ReactionData> = mapOf(),

    val error: UiError? = null,
) {
    data class ReactionData(
        val total: Int,
        val page: Int = -1,
        val complete: Boolean = false,
        val users: List<PortalUserRecord> = listOf(),
    )
}
