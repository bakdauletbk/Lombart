package kz.pillikan.lombart.content.model.response.notifications

import com.google.gson.annotations.SerializedName

data class DataList(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("description")
    val description: String? = null,
    @SerializedName("created_at")
    val created_at: String? = null
)