package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName

data class SignInRequest(
    @SerializedName("iin")
    val iin: String? = null,
    @SerializedName("ftoken")
    val ftoken: String? = null,
    @SerializedName("password")
    val password: String? = null
)