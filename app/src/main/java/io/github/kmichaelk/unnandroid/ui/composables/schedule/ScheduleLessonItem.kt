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

package io.github.kmichaelk.unnandroid.ui.composables.schedule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleLesson
import io.github.kmichaelk.unnandroid.ui.composables.IconText
import io.github.kmichaelk.unnandroid.ui.extensions.color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ScheduleLessonItem(
    lesson: ScheduleLesson,
    snackBarHostState: SnackbarHostState,
    snackbarScope: CoroutineScope
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.size(48.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lesson.color()
                        ?: MaterialTheme.colorScheme.surfaceContainerHigh
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        lesson.number.toString(),
                        color = Color.Black.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(lesson.begin)
            Text(lesson.end)
        }
        Column {
            Text(
                lesson.discipline,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    snackbarScope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        snackBarHostState.showSnackbar(lesson.discipline)
                    }
                }
            )
            Text(
                lesson.kind ?: "Нет данных",
                fontWeight = FontWeight.Light,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(Modifier.height(2.dp))

            ScheduleLessonItemDetail(
                icon = Icons.Default.LocationOn,
                caption = "Место проведения",
                text = "${lesson.auditorium} (${lesson.building})",
                snackBarHostState = snackBarHostState,
                snackbarScope = snackbarScope
            )
            ScheduleLessonItemDetail(
                icon = Icons.AutoMirrored.Filled.DirectionsWalk,
                caption = "Поток",
                text = lesson.stream ?: "Нет данных",
                snackBarHostState = snackBarHostState,
                snackbarScope = snackbarScope
            )
            ScheduleLessonItemDetail(
                icon = Icons.Default.Person,
                caption = "Преподаватель",
                text = (lesson.lecturer
                    ?: "Нет данных") + (if (lesson.lecturerRank != null) " (${lesson.lecturerRank})" else ""),
                snackBarHostState = snackBarHostState,
                snackbarScope = snackbarScope
            )
        }
    }
}

@Composable
fun ScheduleLessonItemDetail(
    icon: ImageVector,
    caption: String,
    text: String,
    snackBarHostState: SnackbarHostState,
    snackbarScope: CoroutineScope
) {
    IconText(
        icon = icon,
        contentDescription = caption,
        text = text,
        size = 15.sp,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .fillMaxWidth()
            .clickable {
                snackbarScope.launch {
                    snackBarHostState.currentSnackbarData?.dismiss()
                    snackBarHostState.showSnackbar(text)
                }
            }
    )
}