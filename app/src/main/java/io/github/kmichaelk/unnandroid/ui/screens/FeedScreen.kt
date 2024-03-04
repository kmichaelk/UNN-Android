package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.LocalImageLoader
import coil.imageLoader
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedUser
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.AppDrawer
import io.github.kmichaelk.unnandroid.ui.composables.FancyError
import io.github.kmichaelk.unnandroid.ui.composables.FancyLoading
import io.github.kmichaelk.unnandroid.ui.composables.FeedPost
import io.github.kmichaelk.unnandroid.ui.extensions.navigate
import io.github.kmichaelk.unnandroid.ui.viewmodels.FeedScreenViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    viewModel: FeedScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val navController = LocalNavController.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val pullToRefreshState = rememberPullToRefreshState()
    val postsBottomSheetState = rememberModalBottomSheetState()

    val uriHandler = LocalUriHandler.current

    viewModel.setBaseImageLoader(LocalContext.current.imageLoader)

    val onUserOpen = { user: PortalFeedUser ->
        viewModel.transformBitrixId(user.bxId) {
            navController.navigate("${AppScreen.User.name}/${it}")
        }
    }

    LaunchedEffect(Unit) {
        if (state.data == null) {
            pullToRefreshState.startRefresh()
        }
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.load().join()
            pullToRefreshState.endRefresh()
        }
    }

    AppDrawer(drawerState = drawerState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Живая лента") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    navigationIcon = {
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
                    actions = {
                        IconButton(onClick = {
                            uriHandler.openUri(PortalService.P_URL)
                        }) {
                            Icon(Icons.Default.OpenInNew, contentDescription = "Открыть в браузере")
                        }
                    }
                )
            },
        ) { paddingValues ->
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .nestedScroll(pullToRefreshState.nestedScrollConnection)
            ) {
                if (state.data == null && state.error != null) {
                    FancyError(state.error!!, onRetry = {
                        pullToRefreshState.startRefresh()
                    })
                } else if (state.data != null) {
                    val posts = state.data!!
                    CompositionLocalProvider(LocalImageLoader provides viewModel.imageLoader) {
                        LazyColumn {
                            items(posts, key = { it.id }) {
                                ElevatedCard(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                    FeedPost(
                                        post = it,
                                        onOpenComments = {
                                            navController.navigate(
                                                route = AppScreen.FeedPost.name,
                                                args = bundleOf("post" to it)
                                            )
                                        },
                                        onUserOpen = onUserOpen,
                                        bottomSheetState = postsBottomSheetState,
                                    )
                                }
                            }
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (state.error != null) {
                                        Button(onClick = { viewModel.loadMore() }) {
                                            Text("Загрузить еще")
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
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = pullToRefreshState,
                )
            }
        }
    }
}