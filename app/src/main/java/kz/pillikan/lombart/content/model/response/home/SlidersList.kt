package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class SlidersList(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("img")
    val img: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("updated_at")
    val updated_at: String? = null
)