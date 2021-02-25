package kz.pillikan.lombart.content.model.request.appeal

import com.google.gson.annotations.SerializedName

data class FeedbackRequest(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("description")
    val description: String? = null
)