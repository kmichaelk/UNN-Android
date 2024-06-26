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

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.DummyAvatar
import io.github.kmichaelk.unnandroid.ui.composables.IconText
import io.github.kmichaelk.unnandroid.ui.viewmodels.StudentSearchScreenViewModel

@Composable
fun StudentsSearchScreen(
    viewModel: StudentSearchScreenViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    UserSearchScreen(
        viewModel = viewModel,
        title = "Поиск по студентам"
    ) {
        ElevatedCard(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .clickable {
                    navController.navigate("${AppScreen.User.name}/${it.id}")
                }
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(Modifier.size(48.dp)) {
                        if (it.avatar.isAvailable()) {
                            AsyncImage(
                                model = PortalService.P_URL + it.avatar.urlThumbnail,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } else {
                            DummyAvatar(Modifier.fillMaxSize())
                        }
                    }
                    Column(Modifier.weight(1f)) {
                        Text(it.name, fontWeight = FontWeight.SemiBold)
                        Text(it.educationLevel, fontWeight = FontWeight.Light, fontSize = 15.sp)
                    }
                }

                Spacer(Modifier.height(12.dp))

                IconText(
                    icon = Icons.Default.HomeWork,
                    text = it.department,
                    contentDescription = "Институт",
                    size = 14.sp,
                )
                IconText(
                    icon = Icons.Default.Directions,
                    text = it.direction,
                    contentDescription = "Направление",
                    size = 14.sp,
                )
                IconText(
                    icon = Icons.Default.Group,
                    text = it.group,
                    contentDescription = "Группа",
                    size = 14.sp,
                )
                IconText(
                    icon = Icons.Default.School,
                    text = "${it.course} курс",
                    contentDescription = "Курс",
                    size = 14.sp,
                )
                IconText(
                    icon = Icons.Default.Visibility,
                    text = "${it.educationForm} форма",
                    contentDescription = "Форма обучения",
                    size = 14.sp,
                )
            }
        }
    }
}