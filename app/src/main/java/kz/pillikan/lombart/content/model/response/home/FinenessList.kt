package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class FinenessList(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("value")
    val value: String? = null
)