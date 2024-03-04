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