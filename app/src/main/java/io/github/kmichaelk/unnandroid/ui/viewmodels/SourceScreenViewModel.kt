package io.github.kmichaelk.unnandroid.ui.viewmodels

import io.github.kmichaelk.unnandroid.api.SourceClient
import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import io.github.kmichaelk.unnandroid.ui.state.SourceScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class SourceScreenViewModel<T>(
    protected val sourceClient: SourceClient,
) : StaticDataScreenViewModel<T>() {

    private val _sUiState = MutableStateFlow(SourceScreenState())
    val sUiState = _sUiState.asStateFlow()

    fun setSemester(semester: SourceSemester) = _sUiState.update { it.copy(
        semester = semester
    ) }

    override suspend fun fetch(): T {
        _uiState.update { it.copy(error = null) }
        if (sUiState.value.semester == null) {
            val semesters = sourceClient.getSemesters().run {
                subList(size - 4, size)
            }
            val current = semesters[semesters.size - 1]
            _sUiState.update { it.copy(
                semester = current,
                semesters = semesters
            ) }
        }
        return fetchSource(sUiState.value.semester!!)
    }

    abstract suspend fun fetchSource(semester: SourceSemester) : T
}