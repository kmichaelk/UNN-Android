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