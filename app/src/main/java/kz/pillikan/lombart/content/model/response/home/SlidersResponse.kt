package kz.pillikan.lombart.content.model.response.home

import com.google.gson.annotations.SerializedName

data class SlidersResponse (
    @SerializedName("sliders")
    val sliders : ArrayList<SlidersList>
)