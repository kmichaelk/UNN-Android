package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material.icons.filled.Scale
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.sourceS.SourceSubjectRef
import io.github.kmichaelk.unnandroid.ui.composables.CollapsibleLazyColumn
import io.github.kmichaelk.unnandroid.ui.composables.CollapsibleSection
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.viewmodels.MarksScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MarksScreen(
    viewModel: MarksScreenViewModel = hiltViewModel()
) {
    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy", Locale("RU"))
    }

    StaticDataScreen(
        viewModel = viewModel,
        title = { Text("Зачетная книжка") }
    ) { data ->
        if (data.isEmpty()) {
            FancyEmpty()
        }
        CollapsibleLazyColumn(
            sections = data.flatMap { it.semesters }.map {
                CollapsibleSection("Семестр ${it.ord}", it.data)
            },
            collapsedByDefault = false
        ) {
            OutlinedCard(
                modifier = Modifier.padding(8.dp)
            ) {
                Column(Modifier.fillMaxSize()) {
                    Row(Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.weight(1f).padding(
                                top = 16.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                        ) {
                            Text(
                                it.subject,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 16.sp,
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if ("Экзамен" == it.controlType)
                                                Color(0xFFFF8080)
                                            else
                                                Color(0xFFFF80C7)
                                        )
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    it.controlType,
                                    fontWeight = FontWeight.Light,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                        if (it.mark.split(".").map(Integer::parseInt)
                                .reduce { acc, l -> acc + l } >= 4
                        ) {
                            Box(Modifier.padding(24.dp)) {
                                Icon(
                                    Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    tint = when (it.mark) {
                                        "4", "4.5" -> LocalContentColor.current.copy(alpha = 0.8f)
                                        "5" -> Color(0xFFFFC107)
                                        "5.5" -> Color(0xFF8BC34A)
                                        else -> LocalContentColor.current
                                    }
                                )
                            }
                        }
                    }
                    if (it.lecturer != null) {
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Преподаватель") },
                            supportingContent = { Text(it.lecturer) }
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
                        supportingContent = { Text(dateFormat.format(it.date)) }
                    )
                    ListItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null
                            )
                        },
                        headlineContent = { Text("Часы") },
                        supportingContent = { Text(it.hours.toString()) }
                    )
                    ListItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.Scale,
                                contentDescription = null
                            )
                        },
                        headlineContent = { Text("Зачетные единицы") },
                        supportingContent = { Text(it.weight.toString()) }
                    )
                    ListItem(
                        leadingContent = {
                            Icon(
                                Icons.Default.Poll,
                                contentDescription = null
                            )
                        },
                        headlineContent = { Text("Результат") },
                        supportingContent = { Text(it.markTitle) }
                    )
                }
            }
        }
    }
}