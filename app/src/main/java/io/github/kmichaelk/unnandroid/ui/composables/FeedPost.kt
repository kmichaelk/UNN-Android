package io.github.kmichaelk.unnandroid.ui.composables

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FeedPost(
    post: PortalFeedPost,
    onDownload: (DownloadManager.Request) -> Unit,
    onUserOpen: (PortalFeedUser) -> Unit,
    onOpenComments: () -> Unit,
    bottomSheetState: SheetState = rememberModalBottomSheetState()
) {
    //val navController = LocalNavController.current
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
                    AsyncImage(
                        model = PortalService.P_URL + it,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } ?: DummyAvatar(Modifier.fillMaxSize())
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
            val pageCount = post.attachmentsUrls.size
            val pagerState = rememberPagerState(pageCount = { pageCount })
            var minHeight by remember { mutableIntStateOf(0) }
            Spacer(Modifier.height(16.dp))
            Box(
                modifier = Modifier.background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                HorizontalPager(
                    state = pagerState,
                    key = { post.attachmentsUrls[it] }
                ) { idx ->
                    Box {
                        AsyncImage(
                            model = PortalService.P_URL + post.attachmentsUrls[idx],
                            contentDescription = null,
                            contentScale = ContentScale.FillWidth,
                            modifier = Modifier
                                .fillMaxWidth()
                                .onSizeChanged { minHeight = minHeight.coerceAtLeast(it.height) },
                        )
                    }
                }
                Spacer(Modifier.requiredHeight(with(LocalDensity.current) { minHeight.toDp() }))
                if (pagerState.pageCount > 1) {
                    Box(
                        Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = (-8).dp, y = (-8).dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.Black.copy(alpha = 0.64f))
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            "${pagerState.currentPage + 1}/${pagerState.pageCount}",
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        if (post.files.isNotEmpty()) {
            val context = LocalContext.current
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 16.dp)) {
                post.files.forEach { file ->
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
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
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
            Column {
                Text(
                    "Получатели",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn(Modifier.fillMaxSize()) {
                    items(post.receivers) {
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
    }
}