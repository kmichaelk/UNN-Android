package io.github.kmichaelk.unnandroid.api.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.github.kmichaelk.unnandroid.models.AppUser
import io.github.kmichaelk.unnandroid.models.AuthCredentials
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthDataHolder @Inject constructor(
    @Named("auth") val authPrefs: SharedPreferences,
) {

    private val gson = Gson()

    private companion object {
        const val PREF_USERNAME = "username"
        const val PREF_PASSWORD = "password"
        const val PREF_USERINFO = "userinfo"
    }

    private val _user = MutableStateFlow(
        if (authPrefs.contains(PREF_USERINFO))
            gson.fromJson(authPrefs.getString(PREF_USERINFO, null), AppUser::class.java)
        else null
    )
    val user = _user.asStateFlow()

    fun setCredentials(credentials: AuthCredentials) = authPrefs.edit {
        putString(PREF_USERNAME, credentials.username)
        putString(PREF_PASSWORD, credentials.password)
    }

    fun logout() {
        authPrefs.edit { clear() }
        _user.update { null }
    }

    fun updateUserInfo(user: AppUser) = run {
        authPrefs.edit { putString(PREF_USERINFO, gson.toJson(user)) }
        _user.update { user }
    }

    fun getCredentials() : AuthCredentials = AuthCredentials(
        authPrefs.getString("username", null)!!,
        authPrefs.getString("password", null)!!
    )
}