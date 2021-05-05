package kz.pillikan.lombart.authorization.model.repository.recovery

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.request.ResetPasswordRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import okhttp3.ResponseBody
import retrofit2.Response

class PasswordRecoveryRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun checkUser(checkUserRequest: CheckUserRequest): Response<CheckResponse> =
        networkService.checkUser(
            appVer = BuildConfig.VERSION_NAME,
            checkUserRequest = checkUserRequest
        )

    suspend fun sendSms(sendSmsRequest: SendSmsRequest): Response<ResponseBody> =
        networkService.sendSms(appVer = BuildConfig.VERSION_NAME, sendSmsRequest)

    suspend fun verificationNumber(checkNumberRequest: CheckNumberRequest): Response<ResponseBody> =
        networkService.verificationNumber(
            appVer = BuildConfig.VERSION_NAME,
            checkNumberRequest = checkNumberRequest
        )


    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Response<ResponseBody> =
        networkService.resetPassword(
            appVer = BuildConfig.VERSION_NAME,
            resetPasswordRequest = resetPasswordRequest
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