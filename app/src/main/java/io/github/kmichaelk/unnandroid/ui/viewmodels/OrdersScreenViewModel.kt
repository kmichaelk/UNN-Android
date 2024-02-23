package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalOrder
import javax.inject.Inject

@HiltViewModel
class OrdersScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : StaticDataScreenViewModel<List<PortalOrder>>() {

    override suspend fun fetch(): List<PortalOrder> =
        portalClient.getOrders()
}