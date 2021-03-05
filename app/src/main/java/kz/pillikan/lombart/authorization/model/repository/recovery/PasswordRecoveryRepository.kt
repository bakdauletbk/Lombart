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
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking

class PasswordRecoveryRepository(application: Application) {

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

    suspend fun verificationNumber(checkNumberRequest: CheckNumberRequest): Boolean {
        val response = networkService.verificationNumber(
            appVer = BuildConfig.VERSION_NAME,
            checkNumberRequest = checkNumberRequest
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Boolean {
        val response = networkService.resetPassword(
            appVer = BuildConfig.VERSION_NAME,
            resetPasswordRequest = resetPasswordRequest
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }
}