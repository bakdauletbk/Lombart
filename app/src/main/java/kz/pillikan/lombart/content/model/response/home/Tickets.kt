package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class Tickets (
    @SerializedName("items")
    val items : ArrayList<TicketsItems> = ArrayList(),
    @SerializedName("ticketInfo")
    val ticketInfo : TicketInfo
)