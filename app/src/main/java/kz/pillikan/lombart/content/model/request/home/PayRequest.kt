package kz.pillikan.lombart.content.model.request.home

import com.google.gson.annotations.SerializedName

data class PayRequest(
    @SerializedName("ticket")
    val ticket: String? = null,
    @SerializedName("amount")
    val amount: String? = null,
    @SerializedName("card_id")
    val card_id: String? = null
)