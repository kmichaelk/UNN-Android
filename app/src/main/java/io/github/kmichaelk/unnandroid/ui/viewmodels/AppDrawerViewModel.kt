package io.github.kmichaelk.unnandroid.ui.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kmichaelk.unnandroid.managers.AuthManager
import javax.inject.Inject

@HiltViewModel
class AppDrawerViewModel @Inject constructor(
    private val authManager: AuthManager
) : ViewModel() {

    val user = authManager.user

    fun logout() = authManager.logout()
}