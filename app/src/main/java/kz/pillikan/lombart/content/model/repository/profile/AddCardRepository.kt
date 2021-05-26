package kz.pillikan.lombart.content.model.repository.profile

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.profile.CardRequest
import kz.pillikan.lombart.content.model.response.profile.CardResponse
import retrofit2.Response

class AddCardRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun addCard(cardRequest: CardRequest): Response<CardResponse> = networkService.addCard(
        Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
        appVer = BuildConfig.VERSION_NAME,
        cardRequest
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