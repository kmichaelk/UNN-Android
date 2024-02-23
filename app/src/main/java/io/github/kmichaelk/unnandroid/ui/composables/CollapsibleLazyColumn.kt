package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> CollapsibleLazyColumn(
    modifier: Modifier = Modifier,
    sections: List<CollapsibleSection<T>>,
    collapsedByDefault: Boolean = true,
    render: @Composable (entry: T) -> Unit
) {
    val collapsedState = remember(sections, collapsedByDefault) {
        sections.map { collapsedByDefault }.toMutableStateList()
    }
    LazyColumn(modifier) {
        sections.forEachIndexed { i, dataItem ->
            val collapsed = collapsedState[i]
            stickyHeader(key = i) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable { collapsedState[i] = !collapsed }
                ) {
                    Icon(
                        Icons.Default.run {
                            if (collapsed)
                                KeyboardArrowDown
                            else
                                KeyboardArrowUp
                        },
                        contentDescription = if (collapsed) "Развернуть" else "Свернуть",
                        tint = LocalContentColor.current.copy(alpha = 0.5f),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        dataItem.title,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .weight(1f)
                    )
                }
            }
            if (!collapsed) {
                items(dataItem.rows) {
                    render(it)
                }
            }
        }
    }
}

data class CollapsibleSection<T>(
    val title: String,
    val rows: List<T>
)