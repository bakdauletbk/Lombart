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
    val latitude: String? = null
)