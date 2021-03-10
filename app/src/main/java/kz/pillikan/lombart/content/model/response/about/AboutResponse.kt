package kz.pillikan.lombart.content.model.response.about

import com.google.gson.annotations.SerializedName

data class AboutResponse(
    @SerializedName("text2")
    val text2: String? = null,
    @SerializedName("text3")
    val text3: String? = null
)