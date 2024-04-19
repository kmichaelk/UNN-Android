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

package io.github.kmichaelk.unnandroid.ui.viewmodels

import coil.ImageLoader
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalUser
import javax.inject.Inject

@HiltViewModel
class UserDetailScreenViewModel @Inject constructor(
    private val portalClient: PortalClient,
) : StaticDataScreenViewModel<PortalUser>() {

    var id: Int = -1

    lateinit var imageLoader: ImageLoader

    fun setBaseImageLoader(imageLoader: ImageLoader) {
        if (::imageLoader.isInitialized) return
        this.imageLoader = imageLoader.newBuilder()
            .okHttpClient(
                portalClient.getOkHttpClient()
                    .newBuilder()
                    .cache(null)
                    .build()
            )
            .build()
    }

    override suspend fun fetch(): PortalUser =
        portalClient.getUser(id)
}