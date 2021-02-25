package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName

data class CheckUserRequest (
    @SerializedName("iin")
    val iin: String? = null
)