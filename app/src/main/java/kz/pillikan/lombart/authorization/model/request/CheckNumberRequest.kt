package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName

data class CheckNumberRequest(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("activationcode")
    val activationcode: String? = null
)