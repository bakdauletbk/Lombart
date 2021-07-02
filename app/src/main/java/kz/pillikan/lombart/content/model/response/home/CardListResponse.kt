package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class CardListResponse (
    @SerializedName("cards")
    val cards : ArrayList<CardList>
)