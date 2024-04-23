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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser
import io.github.kmichaelk.unnandroid.ui.composables.HtmlText
import io.github.kmichaelk.unnandroid.ui.composables.ImageSlider
import io.github.kmichaelk.unnandroid.ui.composables.feed.atoms.FeedAttachedFileLink
import io.github.kmichaelk.unnandroid.ui.composables.feed.atoms.FeedAvatar

@Composable
fun FeedPostComment(
    comment: PortalFeedComment,
    onUserOpen: (PortalFeedUser) -> Unit,
    onDownload: (DownloadManager.Request) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .clickable { onUserOpen(comment.author) }
                    .padding(12.dp),
            ) {
                Box(Modifier.size(48.dp)) {
                    FeedAvatar(url = comment.author.avatarUrl)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(comment.author.name, fontSize = 14.sp)
                    Text(comment.datetime, fontSize = 12.sp, lineHeight = 12.sp, fontWeight = FontWeight.Light)
                }
            }
            Spacer(Modifier.height(4.dp))

            Box(Modifier.padding(horizontal = 12.dp)) {
                HtmlText(comment.html, onClicked = {
                    try {
                        uriHandler.openUri(it)
                    } catch (ignore: Exception) {}
                })
            }

            if (comment.attachmentsUrls.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                comment.attachmentsUrls.forEach { println("cmnt: '${comment}'") }
                ImageSlider(
                    imageUrls = comment.attachmentsUrls.map { PortalService.P_URL + it },
                )
            }

            Spacer(Modifier.height(4.dp))

            if (comment.files.isNotEmpty()) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    comment.files.forEach {
                        FeedAttachedFileLink(
                            file = it,
                            onDownload = onDownload,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}