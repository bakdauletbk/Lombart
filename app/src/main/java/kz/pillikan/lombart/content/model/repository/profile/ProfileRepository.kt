package kz.pillikan.lombart.content.model.repository.profile

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.response.home.ProfileInfo
import kz.pillikan.lombart.content.model.response.profile.CardModel

class ProfileRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)


    suspend fun getCard(): List<CardModel?> {
        return try {
            val cards: ArrayList<CardModel> = ArrayList()
            cards.add(CardModel("Kaspi Bank", 8484515115151))
            cards.add(CardModel("Master Card", 8484515115151))
            return cards
        } catch (e: Exception) {
            val cards: ArrayList<CardModel> = ArrayList()
            cards.clear()
            return cards
        }
    }

    suspend fun getProfile(): ProfileInfo? {
        val response = networkService.profileInfo(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()?.profile
        } else {
            null
        }
    }

    fun logout(): Boolean {
        return try {
            sessionManager.clear()
            true
        } catch (e: Exception) {
            false
        }
    }

}