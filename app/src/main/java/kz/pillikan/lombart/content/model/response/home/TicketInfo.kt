package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class TicketInfo(
    @SerializedName("Number")
    val Number: String? = null,
    @SerializedName("BaseName")
    val BaseName: String? = null,
    @SerializedName("PossibilityOfExtension")
    val PossibilityOfExtension: String? = null,
    @SerializedName("Award")
    val Award: String? = null,
    @SerializedName("Penalty")
    val Penalty: String? = null,
    @SerializedName("TotalPayment")
    val TotalPayment: String? = null,
    @SerializedName("StartDate")
    val StartDate: String? = null,
    @SerializedName("EndDate")
    val EndDate: String? = null,
    @SerializedName("WaitDate")
    val WaitDate: String? = null ,
    @SerializedName("totalDebt")
    val totalDebt: String? = null
)