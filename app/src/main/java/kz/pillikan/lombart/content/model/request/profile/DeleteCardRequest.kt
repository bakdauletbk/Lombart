package kz.pillikan.lombart.content.model.request.profile

import com.google.gson.annotations.SerializedName

data class DeleteCardRequest(
    @SerializedName("id")
    val id: String? = null
)