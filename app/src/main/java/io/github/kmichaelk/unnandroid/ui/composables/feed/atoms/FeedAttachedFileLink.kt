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

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedAttachedFile
import io.github.kmichaelk.unnandroid.ui.composables.IconText

@Composable
fun FeedAttachedFileLink(
    file: PortalFeedAttachedFile,
    onDownload: (DownloadManager.Request) -> Unit,
) {
    val context = LocalContext.current
    IconText(
        icon = Icons.Default.AttachFile,
        text = "${file.title} (${file.size})",
        contentDescription = null,
        maxLines = 3,
        size = 14.sp,
        lineHeight = 14.sp,
        fontWeight = FontWeight.Light,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .fillMaxWidth()
            .clickable {
                // uriHandler.openUri(PortalService.P_URL + file.url)
                // TODO: Android < 10 (Q) Support
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val uri = Uri.parse(PortalService.P_URL + file.url)
                    val downloadManager =
                        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val request = DownloadManager
                        .Request(uri)
                        .apply {
                            setTitle(file.title)
                            setDescription("Загрузка вложения (${file.size})")
                            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            setDestinationInExternalPublicDir(
                                Environment.DIRECTORY_DOWNLOADS,
                                file.title
                            )
                        }
                    onDownload(request)
                    Toast
                        .makeText(
                            context,
                            "Загрузка файла начата",
                            Toast.LENGTH_LONG
                        )
                        .show()
                    downloadManager.enqueue(request)
                }
            }
            .padding(vertical = 4.dp)
    )
}