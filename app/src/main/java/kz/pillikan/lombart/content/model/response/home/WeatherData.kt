package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("temp")
    val temp: String? = null,
    @SerializedName("icon")
    val icon: String? = null
)