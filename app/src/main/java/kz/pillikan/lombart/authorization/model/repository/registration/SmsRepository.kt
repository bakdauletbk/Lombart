package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import okhttp3.ResponseBody
import retrofit2.Response

class SmsRepository(application: Application) {

    companion object {
        const val TAG = "SmsRepository"
    }

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun verificationSms(checkNumberRequest: CheckNumberRequest): Response<ResponseBody> =
        networkService.verificationNumber(
            appVer = BuildConfig.VERSION_NAME,
            checkNumberRequest = checkNumberRequest
        )

    suspend fun sendSms(sendSmsRequest: SendSmsRequest): Response<ResponseBody> =
        networkService.sendSms(
            appVer = BuildConfig.VERSION_NAME,
            sendSmsRequest = sendSmsRequest
        )

    fun clearSharedPref(): Boolean {
        return try {
            sessionManager.clear()
            true
        } catch (e: Exception) {
            false
        }
    }

}