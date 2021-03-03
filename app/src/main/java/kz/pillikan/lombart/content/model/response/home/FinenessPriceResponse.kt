package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class FinenessPriceResponse(
    @SerializedName("prices")
    val prices: ArrayList<FinenessList>,
    @SerializedName("percent1")
    val percent1: String? = null,
    @SerializedName("percent2")
    val percent2: String? = null,
    @SerializedName("amount_limit")
    val amount_limit: Long = 0
)