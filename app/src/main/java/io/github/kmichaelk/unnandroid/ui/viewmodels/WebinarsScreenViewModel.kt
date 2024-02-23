package io.github.kmichaelk.unnandroid.ui.viewmodels

import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.SourceClient
import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import io.github.kmichaelk.unnandroid.models.source.SourceWebinar
import javax.inject.Inject

@HiltViewModel
class WebinarsScreenViewModel @Inject constructor(
    sourceClient: SourceClient
) : SourceScreenViewModel<List<SourceWebinar>>(sourceClient) {

    override suspend fun fetchSource(semester: SourceSemester): List<SourceWebinar> =
        sourceClient.getWebinars(semester.ord, semester.yearId)
}