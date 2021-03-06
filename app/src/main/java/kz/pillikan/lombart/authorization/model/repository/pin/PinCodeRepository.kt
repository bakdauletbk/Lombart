package kz.pillikan.lombart.authorization.model.repository.pin

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.PinCodeRequest
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import okhttp3.ResponseBody
import retrofit2.Response


class PinCodeRepository(application: Application) {

    companion object {
        const val TAG = "PinCodeRepository"
    }

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun savePinCode(pinCodeRequest: PinCodeRequest): Response<ResponseBody> {
        return networkService.pinCode(
            appVer = BuildConfig.VERSION_NAME,
            Authorization = Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            pinCodeRequest = pinCodeRequest
        )
    }

    fun clearSharedPref(): Boolean {
        return try {
            sessionManager.clear()
            true
        } catch (e: Exception) {
            false
        }
    }

}