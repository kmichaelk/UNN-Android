package io.github.kmichaelk.unnandroid.api.service

import io.github.kmichaelk.unnandroid.models.source.SourceSemester
import io.github.kmichaelk.unnandroid.models.source.SourceSubjectData
import io.github.kmichaelk.unnandroid.models.source.SourceWebinar
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SourceService {

    companion object {
        const val BASE_URL = "https://source.unn.ru/"

        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    @POST("/")
    @FormUrlEncoded
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String,

        @Field("action") action: String = "log-in",
        @Field("submit") wtf: String = "Войти",
    ) : Response<Unit>

    @GET("ajax/get/check_session.php")
    suspend fun checkSession(
        @Header("Cookie") sessionIdCookie: String
    ) : Boolean

    @GET("#/student/materials")
    suspend fun getSemesters() : List<SourceSemester>

    @GET("ajax/get/materials.php")
    suspend fun getMaterials(
        @Query("semester") semester: Int,
        @Query("year") year: Int,
    ) : Map<String, SourceSubjectData>

    @GET("ajax/get/webinars.php")
    suspend fun getWebinars(
        @Query("semester") semester: Int,
        @Query("year") year: Int,
    ) : List<SourceWebinar>
}