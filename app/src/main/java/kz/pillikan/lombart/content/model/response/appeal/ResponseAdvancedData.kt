package kz.pillikan.lombart.content.model.response.appeal

import com.google.gson.annotations.SerializedName

data class ResponseAdvancedData(
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("phone")
    val phone: String? = null
)