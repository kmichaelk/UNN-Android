package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.SourceClient
import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import io.github.kmichaelk.unnandroid.models.source.SourceSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MaterialsScreenViewModel @Inject constructor(
    sourceClient: SourceClient
) : SourceScreenViewModel<List<SourceSubject>>(sourceClient) {

    override suspend fun fetchSource(semester: SourceSemester): List<SourceSubject> =
        sourceClient.getMaterials(semester.ord, semester.yearId)
}