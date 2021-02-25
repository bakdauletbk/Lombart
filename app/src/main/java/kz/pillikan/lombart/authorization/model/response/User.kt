package kz.pillikan.lombart.authorization.model.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class User(
    @SerializedName("iin")
    val iin: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("access_token")
    val access_token: String? = null,
):Serializable