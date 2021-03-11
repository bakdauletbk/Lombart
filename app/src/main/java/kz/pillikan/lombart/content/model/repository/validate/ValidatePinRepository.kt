package kz.pillikan.lombart.content.model.repository.validate

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.helpers.NetworkHelpers
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.home.ValidatePinRequest

class ValidatePinRepository(application: Application) {

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()


    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun validatePin(validatePinRequest: ValidatePinRequest): Boolean {
        val response = networkService.validatePin(
            validatePinRequest = validatePinRequest,
            appVer = BuildConfig.VERSION_NAME,
            Authorization = Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken()
        )
        return response.code() == Constants.RESPONSE_SUCCESS_CODE
    }

    suspend fun checkNetwork(context: Context): Boolean {
        return NetworkHelpers.isNetworkConection(context)
    }

}