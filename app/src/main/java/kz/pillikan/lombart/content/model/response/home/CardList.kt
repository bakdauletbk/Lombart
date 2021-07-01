package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class CardList(
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("card_hash")
    val card_hash: String? = null
)