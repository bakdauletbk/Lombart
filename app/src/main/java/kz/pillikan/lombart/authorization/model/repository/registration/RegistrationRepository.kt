package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import retrofit2.Response

class RegistrationRepository(application: Application) {

    companion object {
        const val TAG = "RegistrationRepository"
    }

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

    fun clearSharedPref(): Boolean {
        return try {
            sessionManager.clear()
            true
        } catch (e: Exception) {
            false
        }
    }

}