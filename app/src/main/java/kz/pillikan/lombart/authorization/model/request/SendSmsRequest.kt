package kz.pillikan.lombart.authorization.model.request

import com.google.gson.annotations.SerializedName

data class SendSmsRequest (
    @SerializedName("phone")
    val phone: String? = null
)