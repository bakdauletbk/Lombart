package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class ProfileInfo(
    @SerializedName("iin")
    val iin: String? = null,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("fio")
    val fio: String? = null,
    @SerializedName("created_at")
    val created_at: String? = null,
    @SerializedName("f_token")
    val f_token: String? = null,
    @SerializedName("app_ver")
    val app_ver: String? = null
)