package io.github.kmichaelk.unnandroid.models.portal

import com.google.gson.annotations.SerializedName

enum class PortalUserType {

    @SerializedName("employee")
    Employee,

    @SerializedName("student")
    Student,

}