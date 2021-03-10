package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class TitleResponse (
    @SerializedName("title")
    val title : String? = null,
    @SerializedName("text1")
    val text1 : String? = null
)