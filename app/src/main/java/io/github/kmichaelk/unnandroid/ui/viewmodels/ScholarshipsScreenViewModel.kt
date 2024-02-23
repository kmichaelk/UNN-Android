package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalScholarship
import javax.inject.Inject

@HiltViewModel
class ScholarshipsScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : StaticDataScreenViewModel<List<PortalScholarship>>() {

    override suspend fun fetch(): List<PortalScholarship> =
        portalClient.getScholarships()
}