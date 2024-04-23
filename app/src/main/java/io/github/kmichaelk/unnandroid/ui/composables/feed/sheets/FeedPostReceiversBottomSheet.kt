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

package io.github.kmichaelk.unnandroid.ui.composables.feed.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser

@Composable
fun FeedPostReceiversBottomSheet(
    receivers: List<PortalFeedPost.Receiver>,
    onUserOpen: (PortalFeedUser) -> Unit,
) {
    Column {
        Text(
            "Получатели",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleMedium
        )
        LazyColumn(Modifier.fillMaxSize()) {
            items(receivers) {
                ListItem(
                    leadingContent = {
                        Icon(
                            when (it.type) {
                                "user" -> Icons.Default.Person
                                "group" -> Icons.Default.Group
                                "department" -> Icons.Default.Groups
                                else -> Icons.Default.Circle
                            },
                            contentDescription = null
                        )
                    },
                    headlineContent = {
                        Text(it.name)
                    },
                    modifier = Modifier.clickable {
                        if (it.type == "user") {
                            onUserOpen(
                                PortalFeedUser(
                                    bxId = it.id,
                                    name = it.name,
                                    avatarUrl = null
                                )
                            )
                        }
                    }
                )
            }
        }
    }
}