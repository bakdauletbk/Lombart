package kz.pillikan.lombart.content.model.response.profile

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CardResponse(
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("Signed_Order_B64")
    val Signed_Order_B64: String? = null,
    @SerializedName("template")
    val template: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("BackLink")
    val BackLink: String? = null,
    @SerializedName("FailureBackLink")
    val FailureBackLink: String? = null,
    @SerializedName("PostLink")
    val PostLink: String? = null,
    @SerializedName("FailurePostLink")
    val FailurePostLink: String? = null,
    @SerializedName("Language")
    val Language: String? = null,
    @SerializedName("appendix")
    val appendix: String? = null
):Serializable