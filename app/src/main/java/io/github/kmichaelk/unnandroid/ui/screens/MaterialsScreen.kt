package io.github.kmichaelk.unnandroid.ui.screens

import android.os.Build
import android.text.format.Formatter
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.sourceS.SourceSubjectRef
import io.github.kmichaelk.unnandroid.ui.composables.CollapsibleLazyColumn
import io.github.kmichaelk.unnandroid.ui.composables.CollapsibleSection
import io.github.kmichaelk.unnandroid.ui.composables.DismissibleSnackbar
import io.github.kmichaelk.unnandroid.ui.composables.IconText
import io.github.kmichaelk.unnandroid.ui.viewmodels.MaterialsScreenViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MaterialsScreen(
    viewModel: MaterialsScreenViewModel = hiltViewModel()
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
        title = { Text("Материалы") },
        snackbarHost = { DismissibleSnackbar(snackBarHostState) },
    ) { data ->
        CollapsibleLazyColumn<SourceSubjectRef>(
            sections = data.map {
                CollapsibleSection(it.name, listOf(
                    (it.data.files ?: emptyList()) as List<SourceSubjectRef>,
                    (it.data.links ?: emptyList()) as List<SourceSubjectRef>
                ).flatten())
            },
            collapsedByDefault = false
        ) {
            when (it) {
                is SourceSubjectRef.File -> ListItem(
                    leadingContent = {
                        Icon(Icons.Default.Description, contentDescription = "Файл")
                    },
                    headlineContent = {
                        Text(it.filename)
                    },
                    supportingContent = {
                        Column(Modifier.fillMaxWidth()) {
                            if (it.comment.isNotBlank()) {
                                Text(it.comment)
                                Spacer(Modifier.height(1.dp))
                            }
                            IconText(
                                icon = Icons.Default.CalendarMonth,
                                text = dateFormat.format(it.date),
                                contentDescription = "Дата загрузки"
                            )
                            IconText(
                                icon = Icons.Default.CloudDownload,
                                text = Formatter.formatShortFileSize(LocalContext.current, it.fileSize),
                                contentDescription = "Размер"
                            )
                        }
                    },
                    modifier = Modifier.combinedClickable(
                        onClick = {
                            uriHandler.openUri(it.getDownloadUrl())
                        },
                        onLongClick = {
                            clipboardManager.setText(AnnotatedString(it.getDownloadUrl()))
                            if (Build.VERSION.SDK_INT < 33) {
                                coroutineScope.launch {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                    snackBarHostState.showSnackbar("Ссылка скопирована")
                                }
                            }
                        }
                    ),
                )
                is SourceSubjectRef.Link -> ListItem(
                    leadingContent = {
                        Icon(Icons.Default.Link, contentDescription = "Ссылка")
                    },
                    headlineContent = {
                        Text(it.link, maxLines = 2)
                    },
                    supportingContent = {
                        Column(Modifier.fillMaxWidth()) {
                            if (it.comment.isNotBlank()) {
                                Text(it.comment)
                                Spacer(Modifier.height(1.dp))
                            }
                            IconText(
                                icon = Icons.Default.CalendarMonth,
                                text = dateFormat.format(it.date),
                                contentDescription = "Дата загрузки"
                            )
                        }
                    },
                    modifier = Modifier.combinedClickable(
                        onClick = {
                            uriHandler.openUri(it.link)
                        },
                        onLongClick = {
                            clipboardManager.setText(AnnotatedString(it.link))
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