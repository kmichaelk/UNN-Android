package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.managers.AuthManager
import io.github.kmichaelk.unnandroid.ui.state.AuthScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthScreenViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthScreenState())
    val uiState = _uiState.asStateFlow()

    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onSubmit() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val isAuthorized: Boolean
            try {
                isAuthorized = authManager.login(_uiState.value.username, _uiState.value.password)
            } catch (ex: IOException) {
                Timber.d("IO auth error", ex)
                _uiState.update { it.copy(
                    error = "Ошибка соединения, попробуйте еще раз",
                    isLoading = false
                ) }
                return@launch
            } catch (ex: Exception) {
                Timber.d("Unknown auth error", ex)
                _uiState.update { it.copy(
                    error = "Неизвестная ошибка, попробуйте еще раз",
                    isLoading = false
                ) }
                return@launch
            }

            _uiState.update { it.copy(
                isLoading = false,
                error = if (isAuthorized) null else "Неправильный логин или пароль",
                finished = isAuthorized
            ) }
        }
    }
}