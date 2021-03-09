package kz.pillikan.lombart.content.model.request.profile

import com.google.gson.annotations.SerializedName

data class CheckPassword(
    @SerializedName("password")
    val password: String? = null
)