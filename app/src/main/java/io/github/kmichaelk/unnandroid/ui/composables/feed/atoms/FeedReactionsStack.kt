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

package io.github.kmichaelk.unnandroid.ui.composables.feed.atoms

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedReaction

@Composable
fun FeedReactionsStack(
    reactions: Map<PortalFeedReaction, Int>,
    size: Dp = 16.dp,
) {
    Row(
        modifier = Modifier.height(size),
        horizontalArrangement = Arrangement.spacedBy(-size / 3)
    ) {
        reactions.keys.take(3).forEach {
            Image(
                painter = painterResource(it.icon),
                contentDescription = it.displayName,
            )
        }
    }
}