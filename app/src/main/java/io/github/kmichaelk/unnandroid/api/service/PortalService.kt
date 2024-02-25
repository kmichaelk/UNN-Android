package io.github.kmichaelk.unnandroid.api.service

import io.github.kmichaelk.unnandroid.models.portal.PortalBitrixAjaxResponse
import io.github.kmichaelk.unnandroid.models.portal.PortalBitrixIdMapping
import io.github.kmichaelk.unnandroid.models.portal.PortalCurrentUser
import io.github.kmichaelk.unnandroid.models.portal.PortalEmployee
import io.github.kmichaelk.unnandroid.models.portal.PortalFeedData
import io.github.kmichaelk.unnandroid.models.portal.PortalMarks
import io.github.kmichaelk.unnandroid.models.portal.PortalOrder
import io.github.kmichaelk.unnandroid.models.portal.PortalPaginatedResults
import io.github.kmichaelk.unnandroid.models.portal.PortalPlan
import io.github.kmichaelk.unnandroid.models.portal.PortalPostCommentsResponse
import io.github.kmichaelk.unnandroid.models.portal.PortalResponseWrapper
import io.github.kmichaelk.unnandroid.models.portal.PortalScholarship
import io.github.kmichaelk.unnandroid.models.portal.PortalSearchRequestBody
import io.github.kmichaelk.unnandroid.models.portal.PortalStudentSearchResult
import io.github.kmichaelk.unnandroid.models.portal.PortalUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PortalService {

    companion object {
        const val BASE_URL = "https://portal.unn.ru/"

        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

        const val P_URL = "https://portal.unn.ru"
    }

    // @InjectSessionIdQueryParam //
    @GET("rest/server.time.json")
    suspend fun verifySessionId(
        @Header("Cookie") cookie: String,
        @Query("sessid") sessionId: String,
    ): PortalResponseWrapper<*>

    @POST("auth/?login=yes")
    @FormUrlEncoded
    suspend fun login(
        @Field("USER_LOGIN") login: String,
        @Field("USER_PASSWORD") password: String,
        @Field("USER_REMEMBER") remember: BitrixBool = BitrixBool.Y,

        @Field("AUTH_FORM") authForm: BitrixBool = BitrixBool.Y,
        @Field("TYPE") type: String = "AUTH",
    ): Response<Unit>

    @InjectSessionIdQueryParam
    @GET("rest/user.current.json")
    suspend fun getCurrentUser(): PortalResponseWrapper<PortalCurrentUser>

    @InjectCsrfTokenHeader
    @POST("bitrix/services/main/ajax.php?action=socialnetwork.api.livefeed.getNextPage")
    @FormUrlEncoded
    suspend fun getFeed(
        @Field("params[PAGE_NUMBER]") pageNumber: Int,
        @Field("params[PREV_PAGE_LOG_ID]") loadedPostsIds: String, // .join("|")
        @Field("params[LAST_LOG_TIMESTAMP]") firstPostLoadTimestamp: Long,

        @Field("params[siteTemplateId]") siteTemplateId: String = "bitrix24",
        @Field("params[useBXMainFilter]") useBxMainFilter: BitrixBool = BitrixBool.N,

        @Field("logajax") logajax: BitrixBool = BitrixBool.Y,
        @Field("noblog") noblog: BitrixBool = BitrixBool.N,

        @Field("c") c: String = "bitrix:socialnetwork.log.ex",
    ): PortalBitrixAjaxResponse<PortalFeedData>

    @InjectCsrfTokenHeader
    @POST("bitrix/services/main/ajax.php?mode=class&c=bitrix%3Asocialnetwork.blog.post.comment&action=navigateComment")
    @FormUrlEncoded
    suspend fun getPostComments(
        @Field("ENTITY_XML_ID") entityXmlId: String,
        @Field("comment_post_id") postId: Int,

        @Field("PAGEN_1") pageNumber: Int = 1,
        @Field("LAST_LOG_TS") timestamp: Int = 0,
        @Field("last_comment_id") lastCommentId: Int = 0,

        @Field("MODE") mode: String = "LIST",
        @Field("scope") scope: String = "web",
        @Field("AJAX_POST") ajaxPost: BitrixBool = BitrixBool.Y,
    ): PortalPostCommentsResponse

    @GET("bitrix/vuz/api/stipends/")
    suspend fun getScholarships(): List<PortalScholarship>

    @GET("bitrix/vuz/api/orders")
    suspend fun getOrders(): List<PortalOrder>

    @GET("bitrix/vuz/api/rups")
    suspend fun getPlan(): List<PortalPlan>

    @GET("bitrix/vuz/api/marks2")
    suspend fun getMarks(): List<PortalMarks>

    @POST("bitrix/vuz/api/profiles/students")
    suspend fun getStudents(
        @Body body: PortalSearchRequestBody
    ): PortalPaginatedResults<PortalStudentSearchResult>

    @POST("bitrix/vuz/api/user/search/employee")
    suspend fun getEmployees(
        @Body body: PortalSearchRequestBody
    ): PortalPaginatedResults<PortalEmployee>

    @GET("bitrix/vuz/api/user/{id}")
    suspend fun getUser(
        @Path("id") id: Int,
    ): PortalUser

    @GET("bitrix/vuz/api/user/bx/{bitrixId}")
    suspend fun getRuzIdFromBitrixId(
        @Path("bitrixId") bitrixId: Int,
    ): PortalBitrixIdMapping

    enum class BitrixBool {
        Y, N;
        companion object { fun from(b: Boolean): BitrixBool = if (b) Y else N; }
        override fun toString(): String = name
    }

    @MustBeDocumented
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class InjectSessionIdQueryParam

    @MustBeDocumented
    @Target(AnnotationTarget.FUNCTION)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class InjectCsrfTokenHeader
}