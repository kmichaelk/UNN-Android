package io.github.kmichaelk.unnandroid.api.service

import io.github.kmichaelk.unnandroid.models.journal.JournalSectionFilterConstraints
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionInfo
import io.github.kmichaelk.unnandroid.models.journal.JournalSectionSemesterStat
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import java.util.Date

interface JournalService {

    companion object {
        const val BASE_URL = "https://journal.unn.ru/"

        const val DATE_FORMAT = "yyyy-MM-dd"
    }

    @POST("auth.php")
    @FormUrlEncoded
    suspend fun login(
        @Field("login") login: String,
        @Field("password") password: String,
    ) : Response<String>

    @GET("section/index.php")
    suspend fun getSectionsStats() : List<JournalSectionSemesterStat>

    @POST("section/getmenu.php")
    @FormUrlEncoded
    suspend fun getSectionsTimetableMenu(
        @Field("date") date: Date,
        @Field("type") type: Int, // SectionsTimetableRequestType
        @Field("view") view: Int,
        @Field("stud") user: String,

        @Field("section") section: Int = 0,
        @Field("lec") lecturer: Int = 0,
        @Field("zd") building: Int = 0,
        @Field("lang") lang: String = "ru",
    ) : JournalSectionFilterConstraints

    @POST("section/schedule.php")
    @FormUrlEncoded
    suspend fun getSectionsTimetable(
        @Field("date") date: Date,
        @Field("type") type: Int, // SectionsTimetableRequestType
        @Field("view") view: Int, // SectionsTimetableRequestView
        @Field("stud") user: String,

        @Field("section") section: Int = 0,
        @Field("lec") lecturer: Int = 0,
        @Field("zd") building: Int = 0,
        @Field("lang") lang: String = "ru",

        @Field("wid") pageWidth: Int = 0, // WTF???
    ) : String

    @POST("section/eventinfo.php")
    @FormUrlEncoded
    suspend fun getSectionInfo(
        @Field("oid") id: String,

        @Field("lang") lang: String = "ru",
    ) : JournalSectionInfo

    @POST("section/setsection.php")
    @FormUrlEncoded
    suspend fun setSectionStatus(
        @Field("raspid") id: String,
        @Field("userid") user: String,
        @Field("typesect") status: Int, // SectionsSubmitType

        @Field("lang") lang: String = "ru",
    ) : String

    enum class SectionsTimetableRequestType(val queryId: Int) {
        Month(0),
        TwoWeeks(1),
        Week(2),
        Day(3)
    }

    enum class SectionsTimetableRequestView(val queryId: Int) {
        Standard(0),
        Time(1),
        List(2),
        Compare(3)
    }

    enum class SectionsSubmitType(val queryId: Int) {
        Enroll(0),
        Remove(1),
    }
}