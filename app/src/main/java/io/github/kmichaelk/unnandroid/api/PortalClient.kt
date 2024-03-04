package io.github.kmichaelk.unnandroid.api

import io.github.kmichaelk.unnandroid.models.portal.PortalCurrentUser
import io.github.kmichaelk.unnandroid.models.portal.PortalEmployee
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedComment
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedPost
import io.github.kmichaelk.unnandroid.models.portal.PortalMarks
import io.github.kmichaelk.unnandroid.models.portal.PortalOrder
import io.github.kmichaelk.unnandroid.models.portal.PortalPaginatedResults
import io.github.kmichaelk.unnandroid.models.portal.PortalPlan
import io.github.kmichaelk.unnandroid.models.portal.PortalScholarship
import io.github.kmichaelk.unnandroid.models.portal.PortalStudentSearchResult
import io.github.kmichaelk.unnandroid.models.portal.PortalUser
import okhttp3.OkHttpClient

interface PortalClient {

    suspend fun getCurrentUser(): PortalCurrentUser

    suspend fun getFeed(
        pageNumber: Int,
        loadedPostsIds: List<Int>,
        firstPostLoadTimestamp: Long,
    ): List<PortalFeedPost>
    suspend fun getPostComments(entityXmlId: String): List<PortalFeedComment>

    suspend fun getScholarships(): List<PortalScholarship>
    suspend fun getOrders(): List<PortalOrder>
    suspend fun getPlan(): List<PortalPlan>
    suspend fun getMarks(): List<PortalMarks>

    suspend fun getStudents(query: String, offset: Int, take: Int): PortalPaginatedResults<PortalStudentSearchResult>
    suspend fun getEmployees(query: String, offset: Int, take: Int): PortalPaginatedResults<PortalEmployee>

    suspend fun getUser(id: Int): PortalUser
    suspend fun getRuzIdFromBitrixId(bitrixId: Int): Int

    fun getOkHttpClient(): OkHttpClient
    fun getSessionId(): String // TODO: Refactor DownloadManager to use OkHttpClient
}