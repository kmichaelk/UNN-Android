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

package io.github.kmichaelk.unnandroid.ui.composables.ex

import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DismissibleSnackbar(
    snackbarHostState: SnackbarHostState
) {
    val dismissSnackbarState = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value != SwipeToDismissBoxValue.Settled) {
            snackbarHostState.currentSnackbarData?.dismiss()
            true
        } else {
            false
        }
    })
    LaunchedEffect(dismissSnackbarState.currentValue) {
        if (dismissSnackbarState.currentValue != SwipeToDismissBoxValue.Settled) {
            dismissSnackbarState.reset()
        }
    }

    SwipeToDismissBox(
        state = dismissSnackbarState,
        backgroundContent = {},
        content = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.imePadding()
            )
        },
    )
}