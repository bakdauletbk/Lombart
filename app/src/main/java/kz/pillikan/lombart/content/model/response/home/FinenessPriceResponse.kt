package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class FinenessPriceResponse(
    @SerializedName("prices")
    val prices: ArrayList<FinenessList>,
    @SerializedName("percent")
    val percent: String? = null
)