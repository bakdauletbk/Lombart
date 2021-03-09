package kz.pillikan.lombart.content.model.response.about

import com.google.gson.annotations.SerializedName

data class AddressResponse (
    @SerializedName("data")
    val data : ArrayList<AddressList>
)