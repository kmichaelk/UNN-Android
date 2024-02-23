package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalEmployee (

    @SerializedName("id")
    override val id: Int,

    @SerializedName("fullname")
    override val name: String,

    @SerializedName("photo")
    override val avatar: PortalAvatarEx,

    @SerializedName("profiles")
    val profiles: List<PortalProfileC>

) : PortalPerson {

    data class PortalProfileC(

        @SerializedName("id")
        val id: Int,

        @SerializedName("department")
        val department: List<PortalDepartment>?,

        @SerializedName("faculty")
        val faculty: PortalFaculty?,

        @SerializedName("job_title")
        val jobTitle: String?,

        @SerializedName("job_type")
        val jobType: String?,

        @SerializedName("type")
        val type: PortalUserType,
    )

}