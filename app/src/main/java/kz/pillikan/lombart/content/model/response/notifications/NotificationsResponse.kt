package kz.pillikan.lombart.content.model.response.notifications

import com.google.gson.annotations.SerializedName

data class NotificationsResponse(
    @SerializedName("pages")
    val pages: String? = null,
    @SerializedName("hasNext")
    val hasNext: Boolean,
    @SerializedName("data")
    val data: ArrayList<DataList>
)