package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

data class PortalStudentSearchResult(

    @SerializedName("id")
    val recordId: Int,

    @SerializedName("user_id")
    override val id: Int,

    @SerializedName("fullname")
    override val name: String,

    @SerializedName("department")
    val department: String,

    @SerializedName("department_id")
    val departmentId: Int,

    @SerializedName("edu_direction")
    val direction: String,

    @SerializedName("edu_direction_id")
    val directionId: Int,

    @SerializedName("edu_form")
    val educationForm: String,

    @SerializedName("edu_level")
    val educationLevel: String,

    @SerializedName("edu_group")
    val group: String,

    @SerializedName("edu_group_id")
    val groupId: Int,

    @SerializedName("edu_course")
    val course: Int,

    @SerializedName("photo")
    override val avatar: PortalAvatarEx

) : PortalPerson
