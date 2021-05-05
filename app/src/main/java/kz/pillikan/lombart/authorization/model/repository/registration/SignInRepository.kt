package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.SignInRequest
import kz.pillikan.lombart.authorization.model.response.SignInResponse
import kz.pillikan.lombart.authorization.model.response.User
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import retrofit2.Response

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

    suspend fun signIn(signInRequest: SignInRequest): Response<SignInResponse> =
        networkService.logIn(
            appVer = BuildConfig.VERSION_NAME,
            signInRequest = signInRequest
        )

    fun saveUser(user: User) {
        user.access_token?.let { sessionManager.setToken(it) }
        user.iin?.let { sessionManager.setInn(it) }
        user.phone?.let { sessionManager.setPhone(it) }
        sessionManager.setIsAuthorize(true)
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