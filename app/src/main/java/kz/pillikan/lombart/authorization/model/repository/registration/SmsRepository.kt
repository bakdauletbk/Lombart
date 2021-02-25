package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking

class SmsRepository(application: Application) {

    companion object {
        const val TAG = "SmsRepository"
    }

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)

    suspend fun verificationSms(checkNumberRequest: CheckNumberRequest): Boolean {
        val response = networkService.verificationNumber(
            appVer = BuildConfig.VERSION_NAME,
            checkNumberRequest = checkNumberRequest
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }

    suspend fun sendSms(sendSmsRequest: SendSmsRequest): Boolean {
        val response = networkService.sendSms(
            appVer = BuildConfig.VERSION_NAME,
            sendSmsRequest = sendSmsRequest
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }


}