package kz.pillikan.lombart.content.model.repository.profile

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.profile.ChangePassword
import kz.pillikan.lombart.content.model.request.profile.CheckPassword

class ChangePasswordRepository(application: Application) {

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun checkPassword(checkPassword: CheckPassword): Boolean {
        val response = networkService.checkPassword(
            Authorization = ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            appVer = BuildConfig.VERSION_NAME,
            checkPassword = checkPassword
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }

    suspend fun changePassword(changePassword: ChangePassword): Boolean {
        val response = networkService.changePassword(
            Authorization = ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            appVer = BuildConfig.VERSION_NAME,
            changePassword = changePassword
        )
        return response.code() == ApiConstants.RESPONSE_SUCCESS_CODE
    }
}