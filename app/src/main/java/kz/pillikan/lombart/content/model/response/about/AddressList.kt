package kz.pillikan.lombart.content.model.response.about

import com.google.gson.annotations.SerializedName

data class AddressList(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("longitude")
    val longitude: String? = null,
    @SerializedName("latitude")
    val latitude: String? = null,
    @SerializedName("work_time")
    val work_time: String? = null,
    @SerializedName("content")
    val content: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("whats_app")
    val whats_app: String? = null
)