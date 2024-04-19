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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentReturned
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.viewmodels.ScholarshipsScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScholarshipsScreen(
    viewModel: ScholarshipsScreenViewModel = hiltViewModel()
) {
    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy", Locale("RU"))
    }

    StaticDataScreen(
        viewModel = viewModel,
        title = { Text("Стипендии") }
    ) { data ->
        if (data.isEmpty()) {
            FancyEmpty()
        }
        LazyColumn(Modifier.fillMaxSize()) {
            item { Spacer(Modifier.height(8.dp)) }
            items(data) {
                OutlinedCard(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Column(Modifier.fillMaxSize()) {
                        Text(
                            it.type,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
                        )
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.MonetizationOn,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Сумма") },
                            supportingContent = { Text("${it.amount} ₽") }
                        )
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.AssignmentReturned,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Приказ") },
                            supportingContent = { Text(it.order) }
                        )
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Дата начала") },
                            supportingContent = { Text(dateFormat.format(it.dateBegin)) }
                        )
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.Stop,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Дата окончания") },
                            supportingContent = { Text(dateFormat.format(it.dateEnd)) }
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}