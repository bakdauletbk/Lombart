package kz.pillikan.lombart.content.model.repository.appeal

import android.app.Application
import android.content.Context
import android.util.Base64
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest

class AppealRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun sendFeedback(feedbackRequest: FeedbackRequest): Boolean {
        val phone = sessionManager.getPhone()
        val name = feedbackRequest.name
        val description = feedbackRequest.description

        //Base64 encode
        val phoneBase64 = Base64.encodeToString(phone?.toByteArray(), Base64.NO_WRAP)
        val nameBase64 = Base64.encodeToString(name?.toByteArray(), Base64.NO_WRAP)
        val descriptionBase64 = Base64.encodeToString(description?.toByteArray(), Base64.NO_WRAP)

        val feedback =
            FeedbackRequest(name = nameBase64, phone = phoneBase64, description = descriptionBase64)

        val response =
            networkService.sendFeedback(
                Authorization = Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
                appVer = BuildConfig.VERSION_NAME,
                feedbackRequest = feedback
            )

        return response.code() == Constants.RESPONSE_SUCCESS_CODE
    }

}