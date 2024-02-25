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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDatePickerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.journal.JournalSection
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.CutoutFloatingActionButton
import io.github.kmichaelk.unnandroid.ui.composables.DismissibleSnackbar
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.composables.FancyError
import io.github.kmichaelk.unnandroid.ui.composables.FancyLoading
import io.github.kmichaelk.unnandroid.ui.composables.IconText
import io.github.kmichaelk.unnandroid.ui.composables.SectionInfoBottomSheet
import io.github.kmichaelk.unnandroid.ui.extensions.popBackStackLifecycleAware
import io.github.kmichaelk.unnandroid.ui.viewmodels.SectionsTimetableScreenViewModel
import io.github.kmichaelk.unnandroid.utils.debounced
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SectionsListScreen(
    viewModel: SectionsTimetableScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    var sectionDetailId by rememberSaveable { mutableStateOf<String?>(null) }
    var datePickerOpen by rememberSaveable { mutableStateOf(false) }
    var filterHolder by remember { mutableStateOf<SectionsTimetableFilterHolder?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val pullToRefreshState = rememberPullToRefreshState()
    val snackBarHostState = remember { SnackbarHostState() }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = state.date.time)

    //
    val dateFormat = remember { SimpleDateFormat("d MMMM", Locale("RU")) }
    val currentDateFormat = remember { SimpleDateFormat("EEEE, d MMMM", Locale("RU")) }
    //

    val navController = LocalNavController.current

    BackHandler {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        pullToRefreshState.startRefresh()
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.load().join()
            pullToRefreshState.endRefresh()
        }
    }

    val refreshDebounced = debounced(800L) {
        pullToRefreshState.startRefresh()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { DismissibleSnackbar(snackBarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Запись на секции") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStackLifecycleAware()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        bottomBar = {
            AnimatedContent(
                targetState = datePickerOpen,
                transitionSpec = {
                    slideInVertically() togetherWith slideOutVertically()
                },
                label = "DatePickerBottomBar",
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
                            DatePicker(
                                modifier = Modifier.padding(top = 36.dp),
                                state = datePickerState,
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
                                            datePickerOpen = false
                                            viewModel.setDate(Date())
                                            pullToRefreshState.startRefresh()
                                        }) {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = "Сброс"
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            "Выбор даты",
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
                            IconButton(
                                onClick = {
                                    val date = Calendar.getInstance().run {
                                        time = state.date
                                        add(Calendar.DATE, -1)
                                        time
                                    }
                                    viewModel.setDate(date)
                                    refreshDebounced()
                                },
                                modifier = Modifier.padding(start = 2.dp)
                            ) {
                                Icon(Icons.Default.ArrowBackIos, contentDescription = "Назад")
                            }

                            Text(
                                currentDateFormat.format(state.date),
                                fontFamily = FontFamily(Typeface.MONOSPACE),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f),
                            )

                            IconButton(
                                onClick = {
                                    val date = Calendar.getInstance().run {
                                        time = state.date
                                        add(Calendar.DATE, 1)
                                        time
                                    }
                                    viewModel.setDate(date)
                                    refreshDebounced()
                                },
                                modifier = Modifier.padding(start = 2.dp)
                            ) {
                                Icon(Icons.Default.ArrowForwardIos, contentDescription = "Вперед")
                            }
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (snackBarHostState.currentSnackbarData == null) {
                Box(
                    Modifier
                        .zIndex(-5f)
                        .offset(y = if (datePickerOpen) 0.dp else (-10).dp)) {
                    if (datePickerOpen) {
                        CutoutFloatingActionButton(
                            onClick = {
                                datePickerOpen = false
                                viewModel.setDate(Date(datePickerState.selectedDateMillis!!))
                                pullToRefreshState.startRefresh()
                            }
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Применить")
                        }
                    } else {
                        CutoutFloatingActionButton(
                            onClick = { datePickerOpen = !datePickerOpen }
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
            if (state.error != null) {
                FancyError(state.error!!, onRetry = {
                    pullToRefreshState.startRefresh()
                })
            } else if (state.filterConstraints != null && state.data != null) {
                val sections = viewModel.filteredSections()
                if (sections.isEmpty()) {
                    FancyEmpty("Секций в этот день нет")
                }
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp, horizontal = 16.dp)
                                .fillMaxWidth(),
                        ) {
                            SectionsTimetableFilterChip(
                                onClick = {
                                    filterHolder = SectionsTimetableFilterHolder(
                                        title = "Секции",
                                        items = state.filterConstraints!!.types,
                                        selectedItem = state.filters.type,
                                        onResult = { res ->
                                            viewModel.updateFilters { it.copy(type = res) }
                                            filterHolder = null
                                        },
                                        onCancel = { filterHolder = null }
                                    )
                                },
                                label = "Все секции",
                                labelSelected = state.filters.type,
                                icon = Icons.Default.DirectionsRun,
                            )
                            SectionsTimetableFilterChip(
                                onClick = {
                                    filterHolder = SectionsTimetableFilterHolder(
                                        title = "Преподаватели",
                                        items = state.filterConstraints!!.trainers,
                                        selectedItem = state.filters.trainer,
                                        onResult = { res ->
                                            viewModel.updateFilters { it.copy(trainer = res) }
                                            filterHolder = null
                                        },
                                        onCancel = { filterHolder = null }
                                    )
                                },
                                label = "Все преподаватели",
                                labelSelected = state.filters.trainer,
                                icon = Icons.Default.Person,
                            )
                            SectionsTimetableFilterChip(
                                onClick = {
                                    filterHolder = SectionsTimetableFilterHolder(
                                        title = "Здания",
                                        items = state.filterConstraints!!.buildings,
                                        selectedItem = state.filters.building,
                                        onResult = { res ->
                                            viewModel.updateFilters { it.copy(building = res) }
                                            filterHolder = null
                                        },
                                        onCancel = { filterHolder = null }
                                    )
                                },
                                label = "Все здания",
                                labelSelected = state.filters.building,
                                icon = Icons.Default.AccountBalance,
                            )
                        }
                    }
                    sections.entries.forEach { (timespan, sections) ->
                        stickyHeader {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                                    .padding(
                                        start = 32.dp,
                                        top = 16.dp,
                                        bottom = 16.dp,
                                        end = 8.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    timespan,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                        }
                        item { Spacer(Modifier.height(8.dp)) }
                        items(sections, key = { it.id }) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = when (it.status) {
                                        JournalSection.Status.Available -> Color(0x3600BCD4)
                                        JournalSection.Status.NotAvailable -> Color(0x36FFC107)
                                        JournalSection.Status.Booked -> Color(0x368BC34A)
                                    }
                                ),
                                modifier = Modifier
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .clickable {
                                        sectionDetailId = it.id.toString()
                                    },
                            ) {
                                Column(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(vertical = 12.dp, horizontal = 16.dp)
                                ) {
                                    Text(
                                        it.type,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                    )
                                    IconText(
                                        icon = Icons.Default.Person,
                                        text = it.trainer,
                                        contentDescription = "Преподаватель"
                                    )
                                    IconText(
                                        icon = Icons.Default.AccountBalance,
                                        text = it.auditorium,
                                        contentDescription = "Место проведения",
                                        maxLines = 2,
                                        verticalAlignment = Alignment.Top,
                                        iconModifier = Modifier.padding(top = 2.dp)
                                    )
                                    IconText(
                                        icon = Icons.Default.CalendarMonth,
                                        text = dateFormat.format(state.data!!.date) + ", ${it.timespan}",
                                        contentDescription = "Дата занятия"
                                    )
                                    if (it.status == JournalSection.Status.Booked) {
                                        Spacer(Modifier.height(4.dp))
                                        IconText(
                                            icon = Icons.Default.Check,
                                            text = "Вы записаны",
                                            contentDescription = null,
                                            fontWeight = FontWeight.Light
                                        )
                                    }
                                }
                            }
                        }
                        item { Spacer(Modifier.height(8.dp)) }
                    }
                    item { Spacer(Modifier.height(24.dp)) }
                }
            } else {
                FancyLoading()
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )
        }
    }

    if (sectionDetailId != null) {
        SectionInfoBottomSheet(
            sectionId = sectionDetailId!!,
            onResult = { msg ->
                if (msg != null) {
                    coroutineScope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        snackBarHostState.showSnackbar(msg)
                    }
                    pullToRefreshState.startRefresh()
                }
                sectionDetailId = null
            }
        )
    }

    if (filterHolder != null) {
        SectionsTimetableFilterBottomSheet(
            title = filterHolder!!.title,
            items = filterHolder!!.items,
            selectedItem = filterHolder!!.selectedItem,
            onResult = filterHolder!!.onResult,
            onCancel = filterHolder!!.onCancel,
        )
    }
}

data class SectionsTimetableFilterHolder(
    val title: String,
    val items: List<String>,
    val selectedItem: String?,
    val onResult: (String?) -> Unit,
    val onCancel: () -> Unit,
)

@Composable
private fun SectionsTimetableFilterChip(
    onClick: () -> Unit,
    label: String,
    labelSelected: String?,
    icon: ImageVector
) {
    FilterChip(
        modifier = Modifier.fillMaxWidth(),
        selected = labelSelected != null,
        onClick = onClick,
        label = { Text(if (labelSelected == null) label else labelSelected) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        trailingIcon = {
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Изменить")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SectionsTimetableFilterBottomSheet(
    title: String,
    items: List<String>,
    selectedItem: String?,
    onResult: (String?) -> Unit,
    onCancel: () -> Unit,
) {
    val modalBottomSheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = modalBottomSheetState
    ) {
        Text(
            title,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
        LazyColumn(Modifier.fillMaxWidth()) {
            items(listOf(listOf(null), items).flatten()) {
                ListItem(
                    leadingContent = {
                        Icon(
                            if (it == selectedItem) Icons.Default.CheckCircleOutline
                                else Icons.Default.CheckBoxOutlineBlank,
                            contentDescription = if (it == selectedItem) "Выбранный"
                                else "Выбрать",
                        )
                    },
                    headlineContent = { Text(it ?: "Все") },
                    modifier = Modifier.clickable {
                        onResult(it)
                    }
                )
            }
        }
    }
}