package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.SignInRequest
import kz.pillikan.lombart.authorization.model.response.User
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking

class SignInRepository(application: Application) {

    companion object {
        const val TAG = "SignInRepository"
    }

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun signIn(signInRequest: SignInRequest): Boolean {
        val response = networkService.logIn(
            appVer = BuildConfig.VERSION_NAME,
            signInRequest = signInRequest
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            saveUser(response.body()!!.user)
            true
        } else {
            sessionManager.clear()
            false
        }
    }

    private fun saveUser(user: User) {
        user.access_token?.let { sessionManager.setToken(it) }
        user.iin?.let { sessionManager.setInn(it) }
        user.phone?.let { sessionManager.setPhone(it) }
        sessionManager.setIsAuthorize(true)
    }

}