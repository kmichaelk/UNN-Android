package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import io.github.kmichaelk.unnandroid.models.portal.PortalPerson
import io.github.kmichaelk.unnandroid.ui.composables.AppDrawer
import io.github.kmichaelk.unnandroid.ui.composables.FancyEmpty
import io.github.kmichaelk.unnandroid.ui.composables.FancyError
import io.github.kmichaelk.unnandroid.ui.composables.FancyLoading
import io.github.kmichaelk.unnandroid.ui.viewmodels.UserSearchScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> UserSearchScreen(
    viewModel: UserSearchScreenViewModel<T>,
    title: String,
    render: @Composable ((entry: T) -> Unit)
) where T : PortalPerson {
    val state by viewModel.uiState.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    LaunchedEffect(Unit) {
        if (results == null) {
            viewModel.load()
        }
    }

    AppDrawer(drawerState = drawerState) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = query,
                        onValueChange = viewModel::onSearchQueryChange,
                        singleLine = true,
                        shape = SearchBarDefaults.inputFieldShape,
                        colors = TextFieldDefaults.colors().copy(
                            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                            focusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text(title) },
                        leadingIcon = {
                            IconButton(
                                modifier = Modifier.padding(start = 4.dp),
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                Icon(Icons.Default.Menu, contentDescription = "Меню")
                            }
                        },
                        trailingIcon = {
                            Box(
                                modifier = Modifier
                                    .padding(end = 4.dp)
                                    .size(48.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (state.isSearching) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = null,
                                    )
                                }
                            }
                        },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                    )
                }
            },
        ) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (results == null && state.error != null) {
                    FancyError(state.error!!, onRetry = {
                        viewModel.load()
                    })
                } else if (results != null) {
                    if (results!!.items.isEmpty()) {
                        FancyEmpty()
                    }
                    LazyColumn(
                        Modifier
                            .fillMaxSize()
                            .imePadding()) {
                        items(results!!.items, key = { it.id }) {
                            render(it)
                        }
                        if ((state.offset + state.perPage) < results!!.total) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (state.error != null) {
                                        Button(onClick = { viewModel.loadMore() }) {
                                            Text("Ошибка, повторить")
                                        }
                                    } else {
                                        CircularProgressIndicator()
                                    }
                                }
                                LaunchedEffect(Unit) {
                                    viewModel.loadMore()
                                }
                            }
                        }
                    }
                } else {
                    FancyLoading()
                }
            }
        }
    }
}