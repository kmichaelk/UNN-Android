/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

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