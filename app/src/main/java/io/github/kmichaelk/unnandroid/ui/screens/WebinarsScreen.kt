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

package io.github.kmichaelk.unnandroid.ui.screens

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.ui.composables.ex.DismissibleSnackbar
import io.github.kmichaelk.unnandroid.ui.viewmodels.WebinarsScreenViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WebinarsScreen(
    viewModel: WebinarsScreenViewModel = hiltViewModel()
) {

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current

    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy, HH:MM", Locale("RU"))
    }

    SourceScreen(
        viewModel = viewModel,
        title = { Text("Онлайн-занятия") },
        snackbarHost = { DismissibleSnackbar(snackBarHostState) },
    ) { data ->
        LazyColumn(Modifier.fillMaxSize()) {
            item { Spacer(Modifier.height(8.dp)) }
            items(data.reversed()) {
                OutlinedCard(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Column(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
                            Text(
                                it.discipline,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                            )
                            Text(
                                it.comment,
                                fontWeight = FontWeight.Light,
                                fontSize = 14.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Дата") },
                            supportingContent = {
                                Text(
                                    dateFormat.format(it.parsedDate),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.Cast,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Адрес трансляции") },
                            supportingContent = {
                                Text(
                                    it.urlStream,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    uriHandler.openUri(it.urlStream)
                                },
                                onLongClick = {
                                    clipboardManager.setText(AnnotatedString(it.urlStream))
                                    if (Build.VERSION.SDK_INT < 33) {
                                        coroutineScope.launch {
                                            snackBarHostState.currentSnackbarData?.dismiss()
                                            snackBarHostState.showSnackbar("Ссылка скопирована")
                                        }
                                    }
                                }
                            ),
                        )
                        if (it.urlRecord.isNotBlank()) {
                            ListItem(
                                leadingContent = {
                                    Icon(
                                        Icons.Default.Videocam,
                                        contentDescription = null
                                    )
                                },
                                headlineContent = { Text("Адрес записи") },
                                supportingContent = {
                                    Text(
                                        it.urlRecord,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.combinedClickable(
                                    onClick = {
                                        uriHandler.openUri(it.urlRecord)
                                    },
                                    onLongClick = {
                                        clipboardManager.setText(AnnotatedString(it.urlRecord))
                                        if (Build.VERSION.SDK_INT < 33) {
                                            coroutineScope.launch {
                                                snackBarHostState.currentSnackbarData?.dismiss()
                                                snackBarHostState.showSnackbar("Ссылка скопирована")
                                            }
                                        }
                                    }
                                ),
                            )
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}