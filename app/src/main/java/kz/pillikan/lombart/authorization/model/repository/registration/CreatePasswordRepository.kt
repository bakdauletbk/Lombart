package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.model.response.SignInResponse
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking

class CreatePasswordRepository(application: Application) {

    companion object {
        const val TAG = "CreatePasswordRepository"
    }

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun createUser(signUpRequest: SignUpRequest): Boolean {
        val response = networkService.createUser(
            appVer = BuildConfig.VERSION_NAME,
            signUpRequest = signUpRequest
        )

        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            saveUser(response.body()!!)
            true
        } else {
            sessionManager.clear()
            false
        }
    }

    private fun saveUser(signInResponse: SignInResponse) {
        signInResponse.user.access_token?.let { sessionManager.setToken(it) }
        signInResponse.user.iin?.let { sessionManager.setInn(it) }
        signInResponse.user.phone?.let { sessionManager.setPhone(it) }
        sessionManager.setIsAuthorize(true)
    }

}