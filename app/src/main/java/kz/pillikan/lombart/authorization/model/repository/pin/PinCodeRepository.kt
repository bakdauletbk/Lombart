package kz.pillikan.lombart.authorization.model.repository.pin

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.PinCodeRequest
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking


class PinCodeRepository(application: Application) {

    companion object {
        const val TAG = "PinCodeRepository"
    }

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun savePinCode(pinCodeRequest: PinCodeRequest): Boolean {
        val response = networkService.pinCode(
            appVer = BuildConfig.VERSION_NAME,
            Authorization = ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            pinCodeRequest = pinCodeRequest
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }

}