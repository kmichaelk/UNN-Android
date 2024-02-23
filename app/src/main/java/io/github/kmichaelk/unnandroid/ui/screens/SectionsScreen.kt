package io.github.kmichaelk.unnandroid.ui.screens

import android.graphics.Typeface
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAddCheck
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionRecord
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.composables.IconText
import io.github.kmichaelk.unnandroid.ui.extensions.popBackStackLifecycleAware
import io.github.kmichaelk.unnandroid.ui.viewmodels.SectionsScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionsScreen(
    viewModel: SectionsScreenViewModel = hiltViewModel()
) {
    val state by viewModel.sUiState.collectAsState()

    val modalBottomSheetState = rememberModalBottomSheetState()
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    BackHandler(enabled = showBottomSheet) {
        showBottomSheet = false
    }

    val dateFormat = remember {
        SimpleDateFormat("d MMMM (EEEE)", Locale("RU"))
    }

    StaticDataScreen(
        viewModel = viewModel,
        bottomBar = {
            BottomAppBar {
                if (state.semester != null) {
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
                            "${state.semester!!.ord} семестр",
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
        title = { Text("Секции") }
    ) { data ->
        val semester = state.semester!!
        Column(Modifier.fillMaxSize()) {
            if (state.semesters!![state.semesters!!.size - 1] == semester) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    val navController = LocalNavController.current
                    OutlinedButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            navController.popBackStackLifecycleAware()
                            navController.navigate(AppScreen.SectionsTimetable.name)
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                        ) {
                            Icon(Icons.Default.DriveFileRenameOutline, contentDescription = null)
                            Text("Записаться")
                        }
                    }
                }
                HorizontalDivider()
            }
            if (data.isEmpty()) {
                FancyEmpty("Посещений нет :(")
            }
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Default.Assignment, contentDescription = null)
                        },
                        headlineContent = { Text("Занятий по учебному плану") },
                        supportingContent = { Text(semester.total.toString()) }
                    )
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Default.PlaylistAddCheck, contentDescription = null)
                        },
                        headlineContent = { Text("Посещено") },
                        supportingContent = {
                            Text(if (semester.attendedSections > 0 && semester.attendedScheduled > 0)
                                "Всего: ${semester.attendedTotal
                                }, секций: ${semester.attendedSections
                                }, по расписанию: ${semester.attendedScheduled}"
                                else semester.attendedTotal.toString())
                        }
                    )
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Default.RemoveCircleOutline, contentDescription = null)
                        },
                        headlineContent = { Text("Пропущено") },
                        supportingContent = { Text(semester.missed.toString()) }
                    )
                    ListItem(
                        leadingContent = {
                            Icon(Icons.Default.DirectionsRun, contentDescription = null)
                        },
                        headlineContent = { Text("Осталось посетить занятий") },
                        supportingContent = { Text(semester.left.toString()) }
                    )
                    HorizontalDivider()
                }
                items(data.reversed()) {
                    ListItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.Circle,
                                contentDescription = null,
                                tint = when (it.mark) {
                                    JournalSectionRecord.Mark.Attended -> Color(0xFF8BC34A)
                                    JournalSectionRecord.Mark.Missed -> Color(0xFFF44336)
                                    JournalSectionRecord.Mark.NotSet -> Color(0xFFFFC107)
                                }
                            )
                        },
                        headlineContent = {
                            Text(it.type)
                        },
                        supportingContent = {
                            Column(Modifier.fillMaxWidth()) {
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
                                    text = dateFormat.format(it.date) + ", ${it.timespan}",
                                    contentDescription = "Дата занятия"
                                )
                                if (it.mark != JournalSectionRecord.Mark.NotSet) {
                                    IconText(
                                        icon = Icons.Default.DirectionsWalk,
                                        text = when (it.mark) {
                                            JournalSectionRecord.Mark.Attended -> "посещено"
                                            JournalSectionRecord.Mark.Missed -> "пропущено"
                                            else -> { throw AssertionError() }
                                        },
                                        contentDescription = "Отметка"
                                    )
                                }
                            }
                        }
                    )
                }
            }
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
                            if (it == state.semester) {
                                Icon(
                                    Icons.Default.CheckCircleOutline,
                                    contentDescription = "Выбранный"
                                )
                            } else {
                                Icon(Icons.Default.CheckBoxOutlineBlank, contentDescription = "")
                            }
                        },
                        headlineContent = {
                            Text("${it.ord} семестр",)
                        },
                        modifier = Modifier.clickable {
                            if (it != state.semester) {
                                showBottomSheet = false
                                viewModel.setSemester(it)
                            }
                        }
                    )
                }
            }
        }
    }
}