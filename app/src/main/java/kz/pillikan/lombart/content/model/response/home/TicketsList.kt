package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class TicketsList (
    @SerializedName("Specification")
    val Specification : String? = null,
    @SerializedName("TotalWeight")
    val TotalWeight : String? = null,
    @SerializedName("GoldWeight")
    val GoldWeight : String? = null,
    @SerializedName("Debt")
    val Debt : String? = null
)