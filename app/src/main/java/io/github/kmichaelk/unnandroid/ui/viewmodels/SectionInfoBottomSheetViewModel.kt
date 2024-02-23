package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.api.JournalClient
import io.github.kmichaelk.unnandroid.ui.state.SectionInfoBottomSheetState
import io.github.kmichaelk.unnandroid.ui.state.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SectionInfoBottomSheetViewModel @Inject constructor(
    private val journalClient: JournalClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(SectionInfoBottomSheetState())
    val uiState = _uiState.asStateFlow()

    fun load(id: String) : Job {
        assert(_uiState.value.isSubmitting.not())
        _uiState.update { it.copy(
            isLoading = true,
            error = null,
            isSuccessful = null,
        ) }
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = journalClient.getSectionInfo(id)
                _uiState.update { it.copy(
                    isLoading = false,
                    data = data
                ) }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update { it.copy(error = UiError.from(ex)) }
            }
        }
    }

    fun submit(status: Boolean) {
        assert(_uiState.value.data != null)
        assert(_uiState.value.isLoading.not())
        _uiState.update { it.copy(
            isSubmitting = true,
            error = null
        ) }
        viewModelScope.launch(Dispatchers.IO) {
            val isSuccessful = journalClient.setSectionStatus(_uiState.value.data!!.id, status)
            _uiState.update { it.copy(
                isSubmitting = false,
                isSuccessful = isSuccessful
            ) }
        }
    }

    // todo: find a better way
    fun resetState() = _uiState.update { SectionInfoBottomSheetState() }
}