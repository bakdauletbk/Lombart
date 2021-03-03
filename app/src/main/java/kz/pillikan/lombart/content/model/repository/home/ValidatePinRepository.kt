package kz.pillikan.lombart.content.model.repository.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.home.ValidatePinRequest

class ValidatePinRepository(application: Application) {

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()


    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun validatePin(validatePinRequest: ValidatePinRequest): Boolean {
        val response = networkService.validatePin(
            validatePinRequest = validatePinRequest,
            appVer = BuildConfig.VERSION_NAME,
            Authorization = ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken()
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }

}