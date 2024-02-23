package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.models.portal.PortalPaginatedResults
import io.github.kmichaelk.unnandroid.models.portal.PortalStudentSearchResult
import javax.inject.Inject

@HiltViewModel
class StudentSearchScreenViewModel @Inject constructor(
    private val portalClient: PortalClient
) : UserSearchScreenViewModel<PortalStudentSearchResult>() {

    override suspend fun fetch(
        query: String,
        offset: Int,
        take: Int
    ): PortalPaginatedResults<PortalStudentSearchResult> =
        portalClient.getStudents(query, offset, take)
}