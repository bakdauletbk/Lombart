package kz.pillikan.lombart.authorization.model.repository.registration

import android.app.Application
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.authorization.model.response.SignInResponse
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import retrofit2.Response

class CreatePasswordRepository(application: Application) {

    companion object {
        const val TAG = "CreatePasswordRepository"
    }

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)


    suspend fun createUser(signUpRequest: SignUpRequest): Response<SignInResponse> =
        networkService.createUser(
            appVer = BuildConfig.VERSION_NAME,
            signUpRequest = signUpRequest
        )

    fun saveUser(signInResponse: SignInResponse) {
        signInResponse.user.access_token?.let { sessionManager.setToken(it) }
        signInResponse.user.iin?.let { sessionManager.setInn(it) }
        signInResponse.user.phone?.let { sessionManager.setPhone(it) }
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