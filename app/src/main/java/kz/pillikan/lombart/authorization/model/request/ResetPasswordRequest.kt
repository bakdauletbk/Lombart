package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("iin")
    val iin: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("password2")
    val password2: String? = null
)