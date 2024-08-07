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

package io.github.kmichaelk.unnandroid.api.impl

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.api.auth.AuthDataHolder
import io.github.kmichaelk.unnandroid.api.auth.PortalAuthInterceptor
import io.github.kmichaelk.unnandroid.api.extractors.PortalFeedExtractor
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalCommentsPage
import io.github.kmichaelk.unnandroid.models.portal.PortalCurrentUser
import io.github.kmichaelk.unnandroid.models.portal.PortalEmployee
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedReaction
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedVoteable
import io.github.kmichaelk.unnandroid.models.portal.PortalMarks
import io.github.kmichaelk.unnandroid.models.portal.PortalOrder
import io.github.kmichaelk.unnandroid.models.portal.PortalPaginatedResults
import io.github.kmichaelk.unnandroid.models.portal.PortalPlan
import io.github.kmichaelk.unnandroid.models.portal.PortalScholarship
import io.github.kmichaelk.unnandroid.models.portal.PortalSearchRequestBody
import io.github.kmichaelk.unnandroid.models.portal.PortalStudentSearchResult
import io.github.kmichaelk.unnandroid.models.portal.PortalUser
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Named

class PortalClientImpl @Inject constructor(
    @Named("auth") private val authPrefs: SharedPreferences,
    @Named("base") baseHttpClient: OkHttpClient,
    @Named("caching") cachingHttpClient: OkHttpClient,
    authDataHolder: AuthDataHolder,
) : PortalClient {

    private val service: PortalService
    private val okHttpClient: OkHttpClient

    private val feedExtractor = PortalFeedExtractor()

    init {
        val retrofit = Retrofit.Builder()
            .client(baseHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().setDateFormat(PortalService.DATE_FORMAT).create()
                )
            )
            .baseUrl(PortalService.BASE_URL)

        val authenticator = PortalAuthInterceptor(
            retrofit.build().create(PortalService::class.java),
            authPrefs,
            authDataHolder
        )

        okHttpClient = cachingHttpClient.newBuilder()
            .addInterceptor(authenticator)
            .build()

        service = retrofit
            .client(okHttpClient)
            .build()
            .create(PortalService::class.java)
    }

    override fun getOkHttpClient(): OkHttpClient = okHttpClient
    override fun getSessionId(): String = authPrefs.getString("portal_phpsessid", "")!!

    override suspend fun getCurrentUser(): PortalCurrentUser =
        service.getCurrentUser().result!!

    override suspend fun getFeed(
        pageNumber: Int,
        loadedPostsIds: List<Int>,
        firstPostLoadTimestamp: Long
    ): List<PortalFeedPost> = feedExtractor.extractPost(service.getFeed(
        pageNumber = pageNumber,
        loadedPostsIds = loadedPostsIds.joinToString("|"),
        firstPostLoadTimestamp = firstPostLoadTimestamp,
    ).data.html)

    override suspend fun getPostComments(
        entityXmlId: String,
        pageNumber: Int
    ): PortalCommentsPage {
        val data = service.getPostComments(
            entityXmlId = entityXmlId,
            postId = Integer.parseInt(entityXmlId.replace("BLOG_", "")),
            pageNumber = pageNumber
        )
        return PortalCommentsPage(
            comments = feedExtractor.extractComments(data.messageList),
            hasPrev = data.navigation.isNotEmpty()
        )
    }

    override suspend fun getReactions(
        entity: PortalFeedVoteable,
        pageNumber: Int,
        reaction: PortalFeedReaction?
    ) = service.getReactions(
        entityType = entity.getEntityType().let {
            it.declaringJavaClass.getField(it.name)
                .getAnnotation(SerializedName::class.java)!!.value
        },
        voteKey = entity.getVoteKey(),
        entityId = entity.id,
        pageNumber = pageNumber + 1,
        reactionFilter = reaction?.let {
            it.declaringJavaClass.getField(it.name)
                .getAnnotation(SerializedName::class.java)!!.value
        } ?: ""
    ).data.items

    override suspend fun getScholarships(): List<PortalScholarship> =
        service.getScholarships()

    override suspend fun getOrders(): List<PortalOrder> =
        service.getOrders()

    override suspend fun getPlan(): List<PortalPlan> =
        service.getPlan()

    override suspend fun getMarks(): List<PortalMarks> =
        service.getMarks()

    override suspend fun getStudents(query: String, offset: Int, take: Int) : PortalPaginatedResults<PortalStudentSearchResult> =
        service.getStudents(PortalSearchRequestBody(
            first = offset,
            rows = take,
            sortField = "fullname",
            sortOrder = 1,
            filters = mapOf(
                "global" to PortalSearchRequestBody.Filter(
                    value = query,
                    matchMode = "contains"
                )
            ),
            globalFilter = query
        ))

    override suspend fun getEmployees(query: String, offset: Int, take: Int) : PortalPaginatedResults<PortalEmployee> =
        service.getEmployees(PortalSearchRequestBody(
            first = offset,
            rows = take,
            sortField = "fullname",
            sortOrder = 1,
            filters = mapOf(
                "global" to PortalSearchRequestBody.Filter(
                    value = query,
                    matchMode = "contains"
                )
            ),
            globalFilter = query
        ))

    override suspend fun getUser(id: Int): PortalUser =
        service.getUser(id)

    override suspend fun getRuzIdFromBitrixId(bitrixId: Int) =
        service.getRuzIdFromBitrixId(bitrixId).id
}