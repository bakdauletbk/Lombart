package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class TicketsResponse (
    @SerializedName("tickets")
    val tickets : ArrayList<Tickets> = ArrayList()
)