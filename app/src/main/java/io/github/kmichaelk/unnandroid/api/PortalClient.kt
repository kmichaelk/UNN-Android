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

package io.github.kmichaelk.unnandroid.api

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
import io.github.kmichaelk.unnandroid.models.portal.PortalStudentSearchResult
import io.github.kmichaelk.unnandroid.models.portal.PortalUser
import io.github.kmichaelk.unnandroid.models.portal.PortalUserRecord
import okhttp3.OkHttpClient

interface PortalClient {

    suspend fun getCurrentUser(): PortalCurrentUser

    suspend fun getFeed(
        pageNumber: Int,
        loadedPostsIds: List<Int>,
        firstPostLoadTimestamp: Long,
    ): List<PortalFeedPost>
    suspend fun getPostComments(
        entityXmlId: String,
        pageNumber: Int,
    ): PortalCommentsPage

    suspend fun getReactions(
        entity: PortalFeedVoteable,
        pageNumber: Int,
        reaction: PortalFeedReaction?,
    ): List<PortalUserRecord>

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