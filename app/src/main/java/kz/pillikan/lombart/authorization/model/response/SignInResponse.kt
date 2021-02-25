package kz.pillikan.lombart.authorization.model.response

import com.google.gson.annotations.SerializedName

data class SignInResponse (
    @SerializedName("user")
    val user : User
)