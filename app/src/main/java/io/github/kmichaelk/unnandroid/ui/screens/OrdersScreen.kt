package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Numbers
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
import io.github.kmichaelk.unnandroid.ui.viewmodels.OrdersScreenViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrdersScreenViewModel = hiltViewModel()
) {
    val dateFormat = remember {
        SimpleDateFormat("d MMMM yyyy", Locale("RU"))
    }

    StaticDataScreen(
        viewModel = viewModel,
        title = { Text("Приказы") }
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
                        Column(modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)) {
                            Text(
                                it.title,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp,
                            )
                            Text(
                                it.description,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp,
                            )
                        }
                        ListItem(
                            leadingContent = {
                                Icon(
                                    Icons.Default.Numbers,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Номер") },
                            supportingContent = { Text(it.number) }
                        )
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
                                    Icons.Default.Stop,
                                    contentDescription = null
                                )
                            },
                            headlineContent = { Text("Дата действия") },
                            supportingContent = { Text(dateFormat.format(it.dateApprove)) }
                        )
                    }
                }
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}