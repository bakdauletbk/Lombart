package kz.pillikan.lombart.content.model.request.profile

import com.google.gson.annotations.SerializedName

data class ChangePassword(
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("password2")
    val password2: String? = null
)