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

package io.github.kmichaelk.unnandroid.ui.composables.feed.sheets

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedVoteable
import io.github.kmichaelk.unnandroid.models.portal.PortalUserRecord
import io.github.kmichaelk.unnandroid.ui.composables.LoadMore
import io.github.kmichaelk.unnandroid.ui.composables.feed.atoms.FeedAvatar
import io.github.kmichaelk.unnandroid.ui.viewmodels.FeedReactionsBottomSheetViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FeedReactionsBottomSheet(
    viewModel: FeedReactionsBottomSheetViewModel = hiltViewModel(),
    entity: PortalFeedVoteable,
    onUserOpen: (PortalUserRecord) -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    onDismiss: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(entity) {
        viewModel.setEntity(entity)
    }

    if (state.entity == null) return

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var targetTabIndex by rememberSaveable { mutableIntStateOf(-1) }
    val pagerState = rememberPagerState { state.data.keys.size }

    val checkLoaded = { idx: Int ->
        val entry = state.data.entries.toTypedArray()[idx]
        if (!entry.value.complete && entry.value.users.isEmpty()) {
            viewModel.loadMore(entry.key)
        }
    }

    LaunchedEffect(selectedTabIndex) {
        pagerState.animateScrollToPage(selectedTabIndex)
        checkLoaded(selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage) {
        if (targetTabIndex != -1) {
            if (targetTabIndex != pagerState.currentPage) {
                return@LaunchedEffect
            }
            targetTabIndex = -1
        }
        selectedTabIndex = pagerState.currentPage
        checkLoaded(selectedTabIndex)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .requiredHeight(512.dp)
        ) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                divider = {}
            ) {
                state.data.entries.forEachIndexed { index, item ->
                    Tab(
                        selected = (index == selectedTabIndex),
                        onClick = {
                            targetTabIndex = index
                            selectedTabIndex = index
                        },
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Image(
                                painter = painterResource(item.key.icon),
                                contentDescription = item.key.displayName,
                                modifier = Modifier.size(24.dp)
                            )
                            Text("${item.value.total}")
                        }
                    }
                }
            }
            HorizontalDivider()
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1.0f),
                verticalAlignment = Alignment.Top,
            ) { idx ->
                val entry = state.data.entries.toTypedArray()[idx]
                val data = entry.value
                LazyColumn(Modifier.fillMaxWidth()) {
                    item { Spacer(Modifier.height(8.dp)) }
                    items(data.users) {
                        ListItem(
                            modifier = Modifier.clickable { onUserOpen(it) },
                            headlineContent = { Text(it.name) },
                            leadingContent = {
                                Box(Modifier.size(32.dp)) {
                                    FeedAvatar(url = it.avatarUrl)
                                }
                            }
                        )
                    }
                    if (!data.complete) {
                        item {
                            LoadMore(
                                onLoadMore = { viewModel.loadMore(entry.key) },
                                error = state.error
                            )
                            LaunchedEffect(Unit) {
                                viewModel.loadMore(entry.key)
                            }
                        }
                    }
                }
            }
        }
    }
}