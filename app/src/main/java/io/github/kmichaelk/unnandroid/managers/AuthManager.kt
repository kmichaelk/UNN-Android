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

package io.github.kmichaelk.unnandroid.managers

import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.api.auth.AuthDataHolder
import io.github.kmichaelk.unnandroid.api.auth.PortalAuthInterceptor
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.AppUser
import io.github.kmichaelk.unnandroid.models.AuthCredentials
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AuthManager @Inject constructor(
    @Named("base") httpClient: OkHttpClient,
    private val holder: AuthDataHolder,
    private val portalClient: PortalClient,
) {

    private val portalService: PortalService

    val user = holder.user

    init {
        portalService = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(PortalService.BASE_URL)
            .build()
            .create(PortalService::class.java)
    }

    suspend fun login(username: String, password: String): Boolean {
        val response = portalService.login(username, password)

        // todo: refactor, AuthClient should not reference PortalAuthInterceptor
        if (!PortalAuthInterceptor.checkAuthResponse(response, holder.authPrefs)) {
            return false
        }

        holder.setCredentials(AuthCredentials(username, password))

        holder.updateUserInfo(AppUser("Загрузка...", "", ""))

        val portalUser = portalClient.getCurrentUser()
        holder.updateUserInfo(AppUser(
            "${portalUser.lastName} ${portalUser.name} ${portalUser.secondName}",
            portalUser.position,
            portalUser.avatarUrl
        ))

        return true
    }

    fun logout() = holder.logout()
}