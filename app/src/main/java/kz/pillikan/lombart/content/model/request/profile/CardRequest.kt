package kz.pillikan.lombart.content.model.request.profile

import com.google.gson.annotations.SerializedName

data class  CardRequest(
    @SerializedName("cardName")
    val cardName: String? = null
)