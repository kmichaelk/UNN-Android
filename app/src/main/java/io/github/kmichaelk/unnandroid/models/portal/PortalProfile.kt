package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalProfile(

    @SerializedName("id")
    val id: Int,

    @SerializedName("department")
    val department: PortalDepartment?,

    @SerializedName("faculty")
    val faculty: PortalFaculty?,

    @SerializedName("job_title")
    val jobTitle: String?,

    @SerializedName("job_type")
    val jobType: String?,

    @SerializedName("manager")
    val manager: Manager?,

    @SerializedName("edu_specialization")
    val eduSpecialization: PortalEduSpecialization?,

    @SerializedName("edu_qualification")
    val eduQualification: PortalEduSpecialization?,

    @SerializedName("edu_direction")
    val eduDirection: PortalEduDirection?,

    @SerializedName("edu_group")
    val eduGroup: PortalEduGroup?,

    @SerializedName("edu_level")
    val eduLevel: String?,

    @SerializedName("edu_year")
    val eduYear: Int,

    @SerializedName("edu_course")
    val eduCourse: Int,

    @SerializedName("edu_status")
    val eduStatus: String?,

    @SerializedName("edu_form")
    val eduForm: String?,

    @SerializedName("work_address")
    val workAddress: String?,

    @SerializedName("type")
    val type: PortalUserType,
) {

    data class Manager(
        @SerializedName("id")
        val id: Int,

        @SerializedName("fullname")
        val name: String,
    )
}
