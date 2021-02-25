package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking

class RegistrationRepository(application: Application) {

    companion object {
        const val TAG = "RegistrationRepository"
    }

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun checkUser(checkUserRequest: CheckUserRequest): CheckResponse? {
        val response = networkService.checkUser(
            appVer = BuildConfig.VERSION_NAME,
            checkUserRequest = checkUserRequest
        )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            response.body()
        } else {
            sessionManager.clear()
            null
        }
    }

    suspend fun sendSms(sendSmsRequest: SendSmsRequest): Boolean {
        val response = networkService.sendSms(appVer = BuildConfig.VERSION_NAME, sendSmsRequest)
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }


}