package io.github.kmichaelk.unnandroid.ui.screens

import android.graphics.Typeface
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.extensions.displayName
import io.github.kmichaelk.unnandroid.ui.viewmodels.SourceScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SourceScreen(
    viewModel: SourceScreenViewModel<List<T>>,
    title: @Composable (() -> Unit),
    snackbarHost: @Composable () -> Unit = {},
    content: @Composable ((data: List<T>) -> Unit),
) {
    val state by viewModel.sUiState.collectAsState()
    val semester = state.semester

    val pullToRefreshState = rememberPullToRefreshState()

    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showBottomSheet) {
        showBottomSheet = false
    }

    StaticDataScreen(
        viewModel = viewModel,
        pullToRefreshState = pullToRefreshState,
        title = title,
        bottomBar = {
            BottomAppBar {
                if (semester != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Spacer(
                            Modifier
                                .size(48.dp)
                                .padding(end = 2.dp)
                        )

                        Text(
                            "${semester.yearBegin}-${semester.yearEnd} / ${semester.ord}",
                            fontFamily = FontFamily(Typeface.MONOSPACE)
                        )

                        IconButton(
                            onClick = { showBottomSheet = true },
                            modifier = Modifier.padding(start = 2.dp)
                        ) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Выбор семестра")
                        }
                    }
                }
            }
        },
        snackbarHost = snackbarHost,
    ) { data ->
        if (data.isEmpty()) {
            FancyEmpty()
        } else {
            content(data)
        }
    }

    if (showBottomSheet) {
        val semesters = state.semesters!!
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = modalBottomSheetState,
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(semesters.reversed()) {
                    ListItem(
                        leadingContent = {
                            if (it == semester) {
                                Icon(Icons.Default.CheckCircleOutline, contentDescription = "Выбранный")
                            } else {
                                Icon(Icons.Default.CheckBoxOutlineBlank, contentDescription = "")
                            }
                        },
                        headlineContent = {
                            Text("${it.yearBegin}-${it.yearEnd} год, ${it.ord} семестр (${it.season.displayName()})",)
                        },
                        modifier = Modifier.clickable {
                            if (it != semester) {
                                showBottomSheet = false
                                viewModel.setSemester(it)
                                pullToRefreshState.startRefresh()
                            }
                        }
                    )
                }
            }
        }
    }
}