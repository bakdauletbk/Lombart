package kz.pillikan.lombart.content.model.request.notifications

import com.google.gson.annotations.SerializedName

data class PageRequest(
    @SerializedName("page")
    val page: String? = null,
    @SerializedName("limit")
    val limit: String? = null
)