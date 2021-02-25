package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName

data class PinCodeRequest(
    @SerializedName("pin1")
    val pin1: String? = null,
    @SerializedName("pin2")
    val pin2: String? = null
)