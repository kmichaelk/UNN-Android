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