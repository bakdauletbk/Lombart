package kz.pillikan.lombart.content.model.request.home

import com.google.gson.annotations.SerializedName

data class ValidatePinRequest(
    @SerializedName("pin")
    val pin: String? = null
)