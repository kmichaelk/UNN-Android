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

package io.github.kmichaelk.unnandroid.ui.composables.feed

import android.app.DownloadManager
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser
import io.github.kmichaelk.unnandroid.ui.composables.HtmlText
import io.github.kmichaelk.unnandroid.ui.composables.ImageSlider
import io.github.kmichaelk.unnandroid.ui.composables.feed.atoms.FeedAttachedFileLink
import io.github.kmichaelk.unnandroid.ui.composables.feed.atoms.FeedAvatar
import io.github.kmichaelk.unnandroid.ui.composables.feed.sheets.FeedPostReceiversBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedPost(
    post: PortalFeedPost,
    onDownload: (DownloadManager.Request) -> Unit,
    onUserOpen: (PortalFeedUser) -> Unit,
    onOpenComments: () -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState()
) {
    val uriHandler = LocalUriHandler.current

    var receiversSheetOpen by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .clickable { onUserOpen(post.author) }
                .padding(12.dp),
        ) {
            Box(Modifier.size(48.dp)) {
                post.author.avatarUrl?.let {
                    FeedAvatar(url = post.author.avatarUrl)
                }
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(post.author.name, fontSize = 14.sp)
                Text(
                    post.datetime,
                    fontSize = 12.sp,
                    lineHeight = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
        Spacer(Modifier.height(4.dp))

        Box(Modifier.padding(horizontal = 12.dp)) {
            HtmlText(post.html, onClicked = {
                try {
                    uriHandler.openUri(it)
                } catch (ignore: Exception) {
                }
            })
        }

        if (post.attachmentsUrls.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            ImageSlider(
                imageUrls = post.attachmentsUrls.map { PortalService.P_URL + it },
            )
        }

        Spacer(Modifier.height(4.dp))

        if (post.files.isNotEmpty()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                post.files.forEach {
                    FeedAttachedFileLink(
                        file = it,
                        onDownload = onDownload,
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                TextButton(onClick = { receiversSheetOpen = true }) {
                    Icon(Icons.Default.People, contentDescription = "Получатели")
                    Spacer(Modifier.width(6.dp))
                    Text("${post.receivers.size}")
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                TextButton(onClick = onOpenComments) {
                    Icon(Icons.Default.Comment, contentDescription = "Комментарии")
                    Spacer(Modifier.width(6.dp))
                    Text("${post.commentsCount}")
                }
            }

            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                TextButton(onClick = { }) {
                    Icon(Icons.Default.RemoveRedEye, contentDescription = "Просмотры")
                    Spacer(Modifier.width(6.dp))
                    Text(post.views.toString())
                }
            }
        }
    }

    if (receiversSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = {
                receiversSheetOpen = false
            },
            sheetState = bottomSheetState,
        ) {
            FeedPostReceiversBottomSheet(
                receivers = post.receivers,
                onUserOpen = onUserOpen,
            )
        }
    }
}