package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SignUpRequest(
    @SerializedName("iin")
    val iin: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("ftoken")
    val ftoken: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("password2")
    val password2: String? = null
):Serializable