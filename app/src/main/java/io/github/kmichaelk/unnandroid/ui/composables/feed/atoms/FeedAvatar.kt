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

package io.github.kmichaelk.unnandroid.ui.composables.feed.atoms

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.ui.composables.DummyAvatar

@Composable
fun FeedAvatar(
    url: String?
) {
    url?.let {
        AsyncImage(
            model = PortalService.P_URL + it,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
        )
    } ?: DummyAvatar(Modifier.fillMaxSize())
}