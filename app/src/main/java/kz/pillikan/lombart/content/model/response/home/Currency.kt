package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class Currency(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("sale")
    val sale: Double? = null,
    @SerializedName("purchase")
    val purchase: Double? = null
)