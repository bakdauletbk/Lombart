package kz.pillikan.lombart.authorization.model.response

import com.google.gson.annotations.SerializedName

data class DataUser(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("fio")
    val fio: String? = null
)