package kz.pillikan.lombart.authorization.model.response

import com.google.gson.annotations.SerializedName

data class CheckResponse(
    @SerializedName("data")
    val data: ArrayList<DataUser>
)