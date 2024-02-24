package io.github.kmichaelk.unnandroid.ui.screens

import android.graphics.Typeface
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleDateRange
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.ui.composables.AppDrawer
import io.github.kmichaelk.unnandroid.ui.composables.CutoutFloatingActionButton
import io.github.kmichaelk.unnandroid.ui.composables.DismissibleSnackbar
import io.github.kmichaelk.unnandroid.ui.composables.FancyNotice
import io.github.kmichaelk.unnandroid.ui.composables.ScheduleLessonItem
import io.github.kmichaelk.unnandroid.ui.composables.ScheduleSearch
import io.github.kmichaelk.unnandroid.ui.extensions.icon
import io.github.kmichaelk.unnandroid.ui.viewmodels.ScheduleScreenViewModel
import io.github.kmichaelk.unnandroid.R
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.composables.FancyError
import io.github.kmichaelk.unnandroid.ui.composables.FancyLoading
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    val modalBottomSheetState = rememberModalBottomSheetState(
        //confirmValueChange = { it != SheetValue.Expanded }
    )
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = state.range.begin.time,
        initialSelectedEndDateMillis = state.range.end.time,
    )

    var dateRangePickerOpen by rememberSaveable { mutableStateOf(false) }
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        pullToRefreshState.startRefresh()
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.reload()?.join()
            pullToRefreshState.endRefresh()
        }
    }

    //
    val dateFormat = remember { SimpleDateFormat("EEEE, d MMMM", Locale("RU")) }
    val rangeDateFormat = remember { SimpleDateFormat("dd/MM", Locale("RU")) }
    //

    BackHandler(enabled = dateRangePickerOpen || showBottomSheet) {
        dateRangePickerOpen = false
        showBottomSheet = false
    }

    AppDrawer(drawerState = drawerState) {
        Scaffold(
            snackbarHost = { DismissibleSnackbar(snackBarHostState) },
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ScheduleSearch(
                        modifier = Modifier.zIndex(10f),
                        leadingIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Меню")
                            }
                        },
                        placeholder = {
                            if (state.entity != null) {
                                Text(
                                    state.entity!!.label,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text("Поиск расписания")
                            }
                        },
                        onSelect = viewModel::load
                    )
                }
            },
            bottomBar = {
                AnimatedContent(
                    targetState = dateRangePickerOpen,
                    transitionSpec = {
                        slideInVertically() togetherWith slideOutVertically()
                    },
                    label = "DateRangePickerBottomBar",
                    modifier = Modifier.zIndex(-10f)
                ) { isDateRangePickerOpen ->
                    if (isDateRangePickerOpen) {
                        Box(
                            Modifier
                                .height(480.dp)
                                .clip(
                                    MaterialTheme.shapes.medium.copy(
                                        bottomEnd = CornerSize(0),
                                        bottomStart = CornerSize(0)
                                    )
                                )
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                        ) {
                            CompositionLocalProvider(LocalDensity provides Density(LocalDensity.current.density * 0.8f)) {
                                DateRangePicker(
                                    modifier = Modifier.padding(top = 36.dp),
                                    state = dateRangePickerState,
                                    title = {},
                                    headline = {
                                        Row(
                                            Modifier
                                                .fillMaxWidth()
                                                .padding(
                                                    start = 12.dp,
                                                    bottom = 16.dp
                                                ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            IconButton(onClick = {
                                                dateRangePickerOpen = false
                                                if (viewModel.setRange(ScheduleDateRange.thisWeek())) {
                                                    pullToRefreshState.startRefresh()
                                                }
                                            }) {
                                                Icon(
                                                    Icons.Default.Refresh,
                                                    contentDescription = "Сброс"
                                                )
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                "Выбор промежутка времени",
                                            )
                                        }
                                    },
                                    showModeToggle = true,
                                )
                            }
                        }
                    } else {
                        BottomAppBar {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Spacer(
                                    Modifier
                                        .size(48.dp)
                                        .padding(end = 2.dp)
                                )

                                Text(rangeDateFormat.format(state.range.begin), fontFamily = FontFamily(
                                    Typeface.MONOSPACE))
                                Spacer(Modifier.weight(1f))
                                Text(rangeDateFormat.format(state.range.end), fontFamily = FontFamily(
                                    Typeface.MONOSPACE))

                                IconButton(
                                    onClick = { showBottomSheet = true },
                                    modifier = Modifier.padding(start = 2.dp)
                                ) {
                                    Icon(Icons.Default.MoreVert, contentDescription = "Опции")
                                }
                            }
                        }
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                if (snackBarHostState.currentSnackbarData == null) {
                    Box(Modifier.zIndex(-5f)) {
                        if (dateRangePickerOpen) {
                            CutoutFloatingActionButton(
                                onClick = {
                                    dateRangePickerOpen = false
                                    viewModel.setRange(
                                        Date(dateRangePickerState.selectedStartDateMillis!!),
                                        Date(dateRangePickerState.selectedEndDateMillis!!)
                                    )
                                    pullToRefreshState.startRefresh()
                                }
                            ) {
                                Icon(Icons.Default.Check, contentDescription = "Применить")
                            }
                        } else {
                            CutoutFloatingActionButton(
                                onClick = { dateRangePickerOpen = !dateRangePickerOpen }
                            ) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "Выбор даты")
                            }
                        }
                    }
                }
            },
        ) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .zIndex(-20f)
                    .nestedScroll(pullToRefreshState.nestedScrollConnection)
            ) {
                state.error?.let {
                    FancyError(
                        error = state.error!!,
                        onRetry = {
                            pullToRefreshState.startRefresh()
                        }
                    )
                } ?: (
                    if (state.entity == null) {
                        FancyNotice(painter = painterResource(id = R.drawable.schedule)) {
                            Text(
                                "Выберите группу для просмотра расписания",
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    } else if (state.schedule == null) {
                        FancyLoading(painter = painterResource(id = R.drawable.schedule))
                    } else {
                        val schedule = state.schedule!!
                        if (schedule.days.isEmpty()) {
                            FancyEmpty(painter = painterResource(R.drawable.schedule))
                        } else {
                            CompositionLocalProvider(
                                LocalDensity provides Density(LocalDensity.current.density * 0.9f)
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                ) {
                                    schedule.days.map { day ->
                                        stickyHeader {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 4.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                OutlinedCard(
                                                    colors = CardDefaults.cardColors(
                                                        containerColor = MaterialTheme.colorScheme.surfaceBright,
                                                    ),
                                                ) {
                                                    Text(
                                                        dateFormat.format(day.date),
                                                        modifier = Modifier.padding(
                                                            horizontal = 8.dp,
                                                            vertical = 4.dp
                                                        ),
                                                    )
                                                }
                                            }
                                        }
                                        items(day.lessons) { lesson ->
                                            ScheduleLessonItem(
                                                lesson = lesson,
                                                snackBarHostState = snackBarHostState,
                                                snackbarScope = coroutineScope
                                            )
                                        }
                                    }
                                    item { Spacer(Modifier.height(32.dp)) }
                                }
                            }
                        }
                    }
                )

                PullToRefreshContainer(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-16).dp),
                    state = pullToRefreshState,
                )
            }
        }
    }

    if (showBottomSheet) {
        val bookmarks by viewModel.bookmarks.collectAsState()
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = modalBottomSheetState,
        ) {
            Column {
                Text(
                    "Закладки",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.titleMedium
                )
                LazyColumn(Modifier.fillMaxSize()) {
                    items(bookmarks) { entity ->
                        ScheduleEntityBookmarkItem(
                            entity = entity,
                            onClick = {
                                if (state.entity != entity) {
                                    viewModel.setEntity(entity)
                                    pullToRefreshState.startRefresh()
                                    showBottomSheet = false
                                }
                            },
                            selected = (entity == state.entity)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxHeight(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val isDefaultBookmark = viewModel.isDefaultBookmark(entity)
                                IconButton(onClick = {
                                    if (!isDefaultBookmark)
                                        viewModel.setBookmarkAsDefault(entity)
                                }) {
                                    if (isDefaultBookmark)
                                        Icon(
                                            Icons.Default.Star,
                                            contentDescription = "По умолчанию"
                                        )
                                    else
                                        Icon(
                                            Icons.Default.StarOutline,
                                            contentDescription = "Назначить по умолчанию"
                                        )
                                }
                                IconButton(onClick = { viewModel.removeBookmark(entity) }) {
                                    Icon(
                                        Icons.Default.DeleteOutline,
                                        contentDescription = "Удалить"
                                    )
                                }
                            }
                        }
                    }
                    if (state.entity != null && !viewModel.isBookmarked(state.entity!!)) {
                        item {
                            Column(Modifier.fillMaxWidth()) {
                                HorizontalDivider()
                                ScheduleEntityBookmarkItem(
                                    state.entity!!,
                                    onClick = {},
                                ) {
                                    IconButton(onClick = { viewModel.addBookmark(state.entity!!) }) {
                                        Icon(Icons.Default.Add, contentDescription = "Добавить")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleEntityBookmarkItem(
    entity: ScheduleEntity,
    onClick: () -> Unit,
    selected: Boolean = false,
    actions: @Composable () -> Unit,
) {
    ListItem(
        leadingContent = {
            Icon(
                entity.scope.icon(),
                contentDescription = entity.scope.displayName,
                tint = if (selected) MaterialTheme.colorScheme.tertiary else LocalContentColor.current
            )
        },
        headlineContent = { Text(entity.label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        supportingContent = {
            Text(
                entity.description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = actions,
        modifier = Modifier.clickable { onClick() },
    )
}