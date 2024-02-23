package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalEmployee
import io.github.kmichaelk.unnandroid.models.portal.PortalPaginatedResults
import javax.inject.Inject

@HiltViewModel
class EmployeeSearchScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : UserSearchScreenViewModel<PortalEmployee>() {

    override suspend fun fetch(
        query: String,
        offset: Int,
        take: Int
    ): PortalPaginatedResults<PortalEmployee> =
        portalClient.getEmployees(query, offset, take)
}