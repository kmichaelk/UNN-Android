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

import android.app.DownloadManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.api.auth.PortalAuthInterceptor
import io.github.kmichaelk.unnandroid.network.HostScopedInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseFeedViewModel(
    private val portalClient: PortalClient,
) : ViewModel() {

    lateinit var imageLoader: ImageLoader

    fun setBaseImageLoader(imageLoader: ImageLoader) {
        if (::imageLoader.isInitialized) return
        this.imageLoader = imageLoader.newBuilder()
            .okHttpClient(
                portalClient.getOkHttpClient()
                    .newBuilder()
                    .apply {
                        interceptors().apply {
                            find { it is PortalAuthInterceptor }?.let {
                                remove(it)
                                add(HostScopedInterceptor(it, "portal.unn.ru"))
                            }
                        }
                    }
                    .cache(null)
                    .build()
            )
            .build()
    }

    fun transformBitrixId(bitrixId: Int, callback: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val id: Int
            try {
                id = portalClient.getRuzIdFromBitrixId(bitrixId)
            } catch (ex: Exception) {
                Timber.e(ex)
                return@launch
            }
            viewModelScope.launch(Dispatchers.Main) {
                callback(id)
            }
        }
    }

    fun authorizeDownload(request: DownloadManager.Request) {
        request.addRequestHeader("Cookie", "PHPSESSID=${portalClient.getSessionId()}")
    }
}