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

package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.models.schedule.ScheduleEntity
import io.github.kmichaelk.unnandroid.ui.extensions.icon
import io.github.kmichaelk.unnandroid.ui.viewmodels.ScheduleSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleSearch(
    modifier: Modifier = Modifier,
    viewModel: ScheduleSearchViewModel = hiltViewModel(),
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    onSelect: (entity: ScheduleEntity) -> Unit,
    force: Boolean = false,
) {
    val query by viewModel.searchQuery.collectAsState()
    val currentQuery by viewModel.currentQuery.collectAsState()
    val results by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val error by viewModel.error.collectAsState()

    var isSearchActive by rememberSaveable { mutableStateOf(force) }
    val _setSearchActive = { isActive: Boolean ->
        isSearchActive = isActive
        if (!isActive) {
            viewModel.onSearchQueryChange("")
        }
    }
    val setSearchActive = setSearchActive@{ isActive: Boolean ->
        if (!isActive && force) return@setSearchActive
        _setSearchActive(isActive)
    }

    SearchBar(
        query = query,
        onQueryChange = viewModel::onSearchQueryChange,
        active = isSearchActive,
        onActiveChange = setSearchActive,
        onSearch = {},
        modifier = modifier.semantics { traversalIndex = -1f },
        placeholder = { if (isSearchActive || placeholder == null) Text("Поиск расписания") else placeholder() },
        leadingIcon = {
            if (!isSearchActive) {
                leadingIcon?.invoke()
            } else {
                IconButton(onClick = {
                    setSearchActive(false)
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            }
        },
        trailingIcon = {
            if (!isSearchActive)
                Icon(Icons.Default.Search, contentDescription = null)
            else if (isSearching)
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
        },
    ) {
        println("isSearching = ${isSearching}, results = ${results}, query = ${query}")
        if (error != null) {
            FancyError(error!!)
        } else if (!isSearching && results.isEmpty() && query == currentQuery) {
            Text(if (query.isBlank())
                "Введите название группы, номер аудитории или имя преподавателя"
                else "Ничего не найдено",
                modifier = Modifier.padding(24.dp)
            )
        }
        LazyColumn(
            Modifier
                .fillMaxHeight()
                .imePadding()
        ) {
            items(results) { entity ->
                ListItem(
                    leadingContent = {
                        Icon(entity.scope.icon(), contentDescription = entity.scope.displayName)
                    },
                    headlineContent = { Text(entity.label) },
                    supportingContent = { Text(entity.description) },
                    modifier = Modifier.clickable {
                        onSelect(entity)
                        _setSearchActive(false)
                    }
                )
            }
        }
    }
}