package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalMarks
import javax.inject.Inject

@HiltViewModel
class MarksScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : StaticDataScreenViewModel<List<PortalMarks>>() {

    override suspend fun fetch(): List<PortalMarks> =
        portalClient.getMarks()
}