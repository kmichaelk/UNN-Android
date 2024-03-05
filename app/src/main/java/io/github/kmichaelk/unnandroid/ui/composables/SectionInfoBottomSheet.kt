package io.github.kmichaelk.unnandroid.ui.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonPinCircle
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import io.github.kmichaelk.unnandroid.ui.viewmodels.SectionInfoBottomSheetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionInfoBottomSheet(
    viewModel: SectionInfoBottomSheetViewModel = hiltViewModel(),
    sectionId: String,
    onResult: (String?) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var mode by remember { mutableStateOf(false) }

    val modalBottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(sectionId) {
        viewModel.load(sectionId)
    }

    LaunchedEffect(state.isSuccessful) {
        val isSuccessful = state.isSuccessful ?: return@LaunchedEffect

        if (isSuccessful) {
            onResult(if (mode) "Вы записались на секцию" else "Вы отписались от секции")
        } else {
            onResult("Ошибка")
        }

        viewModel.resetState()
    }

    BackHandler(enabled = !state.isSubmitting) {
        onResult(null)
    }

    ModalBottomSheet(
        modifier = Modifier.requiredHeight(800.dp),
        onDismissRequest = {
            if (!state.isSubmitting)
                onResult(null)
        },
        sheetState = modalBottomSheetState
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.height(500.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@ModalBottomSheet
        }
        if (state.error != null) {
            FancyError(error = state.error!!)
            return@ModalBottomSheet
        }
        if (state.data == null) {
            return@ModalBottomSheet
        }
        val section = state.data!!
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    section.type,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    "${section.attendDay} / ${section.trainerMax}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(12.dp))

            IconText(
                icon = Icons.Default.Person,
                text = section.trainer,
                contentDescription = "Преподаватель",
            )

            Spacer(Modifier.height(8.dp))

            IconText(
                icon = Icons.Default.AccountBalance,
                text = section.auditorium,
                contentDescription = "Место проведения",
                maxLines = 2,
                verticalAlignment = Alignment.Top,
                iconModifier = Modifier.padding(top = 4.dp),
            )
            IconText(
                icon = Icons.Default.CalendarMonth,
                text = section.time,
                contentDescription = "Дата проведения",
            )

            Spacer(Modifier.height(8.dp))

            IconText(
                icon = Icons.Default.School,
                text = section.faculty,
                contentDescription = "Факультет",
            )
            IconText(
                icon = Icons.Default.Groups,
                text = section.groups,
                contentDescription = "Группы",
            )

            Spacer(Modifier.height(8.dp))

            IconText(
                icon = Icons.Default.PersonPinCircle,
                text = "В это время в аудитории: ${section.attendTime} / ${section.auditoriumCapacity}",
                contentDescription = null,
            )

            Spacer(Modifier.height(8.dp))

            Spacer(Modifier.height(16.dp))

            when (section.status) {
                JournalSectionInfo.Status.Available -> SectionInfoBottomSheetButton(
                    onClick = {
                        mode = true
                        viewModel.submit(mode)
                    },
                    icon = Icons.Default.AddCircleOutline,
                    text = "Записаться",
                    color = Color(0xFF4CAF50),
                    progress = state.isSubmitting,
                )
                JournalSectionInfo.Status.Booked -> SectionInfoBottomSheetButton(
                    onClick = {
                        mode = false
                        viewModel.submit(mode)
                    },
                    icon = Icons.Default.RemoveCircleOutline,
                    text = "Отменить запись",
                    color = Color(0xFFFF5252),
                    progress = state.isSubmitting,
                )
                else -> Text(
                    if (section.denialNotice.isEmpty())
                    "Записаться или отписаться от данной секции нельзя"
                    else section.denialNotice,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionInfoBottomSheetButton(
    onClick: () -> Unit,
    icon: ImageVector,
    text: String,
    color: Color,
    progress: Boolean
) {
    Button(
        onClick = onClick,
        enabled = !progress,
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (progress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = LocalContentColor.current,
                )
            } else {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(text)
        }
    }
}