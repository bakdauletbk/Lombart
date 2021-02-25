package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class ProfileResponse (
    @SerializedName("profile")
    val profile : ProfileInfo
)