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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.LocalImageLoader
import coil.imageLoader
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalUserRecord
import io.github.kmichaelk.unnandroid.ui.AppScreen
import io.github.kmichaelk.unnandroid.ui.LocalNavController
import io.github.kmichaelk.unnandroid.ui.composables.FancyError
import io.github.kmichaelk.unnandroid.ui.composables.LoadMore
import io.github.kmichaelk.unnandroid.ui.composables.feed.FeedPost
import io.github.kmichaelk.unnandroid.ui.composables.feed.FeedPostComment
import io.github.kmichaelk.unnandroid.ui.extensions.popBackStackLifecycleAware
import io.github.kmichaelk.unnandroid.ui.viewmodels.PostScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    viewModel: PostScreenViewModel = hiltViewModel(),
    post: PortalFeedPost,
) {
    val state by viewModel.uiState.collectAsState()

    val navController = LocalNavController.current
    val pullToRefreshState = rememberPullToRefreshState()

    val uriHandler = LocalUriHandler.current

    viewModel.setBaseImageLoader(LocalContext.current.imageLoader)

    val onUserOpen = { user: PortalUserRecord ->
        viewModel.transformBitrixId(user.bxId) {
            navController.navigate("${AppScreen.User.name}/${it}")
        }
    }

    LaunchedEffect(post) {
        if (state.data == null) {
            pullToRefreshState.startRefresh()
        }
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.load(post).join()
            pullToRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Пост") },
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
                },
                actions = {
                    IconButton(onClick = {
                        uriHandler.openUri(PortalService.P_URL + post.url)
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
            CompositionLocalProvider(LocalImageLoader provides viewModel.imageLoader) {
                LazyColumn {
                    item {
                        FeedPost(
                            post = post,
                            onUserOpen = onUserOpen,
                            onOpenComments = {},
                            onDownload = viewModel::authorizeDownload
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                    state.data?.let { comments ->
                        items(comments, key = { it.id }) {
                            ElevatedCard(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 8.dp)
                            ) {
                                FeedPostComment(
                                    comment = it,
                                    onUserOpen = onUserOpen,
                                    onDownload = viewModel::authorizeDownload
                                )
                            }
                        }
                        if (state.page < state.totalPages) {
                            item {
                                LoadMore(
                                    onLoadMore = { viewModel.loadMore() },
                                    error = state.error
                                )
                                LaunchedEffect(Unit) {
                                    viewModel.loadMore()
                                }
                            }
                        }
                        item {
                            Spacer(Modifier.height(32.dp))
                        }
                    } ?: item {
                        Spacer(Modifier.height(24.dp))
                        state.error?.let {
                            FancyError(it, onRetry = {
                                pullToRefreshState.startRefresh()
                            })
                        } ?: CircularProgressIndicator()
                    }
                }
                PullToRefreshContainer(
                    modifier = Modifier.align(Alignment.TopCenter),
                    state = pullToRefreshState,
                )
            }
        }
    }
}