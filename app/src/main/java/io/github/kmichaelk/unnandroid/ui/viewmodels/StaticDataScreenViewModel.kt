package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.ui.state.StaticDataScreenState
import io.github.kmichaelk.unnandroid.ui.state.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class StaticDataScreenViewModel<T> : ViewModel() {

    protected val _uiState = MutableStateFlow(StaticDataScreenState<T>())
    val uiState = _uiState.asStateFlow()

    fun load() : Job {
        _uiState.update { it.copy(error = null) }
        return viewModelScope.launch(Dispatchers.IO) {
            try {
                val data = fetch()
                _uiState.update { it.copy(
                    data = data
                ) }
            } catch (ex: Exception) {
                Timber.e(ex)
                _uiState.update { it.copy(
                    error = UiError.from(ex),
                ) }
            }
        }
    }

    protected abstract suspend fun fetch() : T
}