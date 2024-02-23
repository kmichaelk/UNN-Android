package io.github.kmichaelk.unnandroid.api.impl

import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import io.github.kmichaelk.unnandroid.api.PortalClient
import io.github.kmichaelk.unnandroid.api.auth.AuthDataHolder
import io.github.kmichaelk.unnandroid.api.auth.PortalAuthInterceptor
import io.github.kmichaelk.unnandroid.api.extractors.PortalFeedExtractor
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalCurrentUser
import io.github.kmichaelk.unnandroid.models.portal.PortalEmployee
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
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

    override suspend fun getCurrentUser(): PortalCurrentUser =
        service.getCurrentUser().result

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
        entityXmlId: String
    ): List<PortalFeedComment> = feedExtractor.extractComments(service.getPostComments(
        entityXmlId = entityXmlId,
        postId = Integer.parseInt(entityXmlId.replace("BLOG_", "")),
    ).messageList)

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