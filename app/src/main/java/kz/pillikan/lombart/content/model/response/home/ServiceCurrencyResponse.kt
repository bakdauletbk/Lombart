package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class ServiceCurrencyResponse(
    @SerializedName("currency")
    val currency : ArrayList<Currency>
)