package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("data")
    val data: WeatherData
)