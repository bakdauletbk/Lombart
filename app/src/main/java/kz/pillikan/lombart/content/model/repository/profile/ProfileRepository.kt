package kz.pillikan.lombart.content.model.repository.profile

import android.app.Application
import android.content.Context
import android.util.Log
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.profile.DeleteCardRequest
import kz.pillikan.lombart.content.model.response.home.CardListResponse
import kz.pillikan.lombart.content.model.response.home.ProfileResponse
import okhttp3.ResponseBody
import retrofit2.Response

class ProfileRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun deleteCard(deleteCardRequest: DeleteCardRequest): Response<Any>{
        return networkService.deleteCard(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME,
            deleteCardRequest
        )
    }

    suspend fun getCard(): Response<CardListResponse> = networkService.getCardList(
        Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
        BuildConfig.VERSION_NAME
    )

    suspend fun getProfile(): Response<ProfileResponse> {
        return networkService.profileInfo(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    fun logout(): Boolean {
        return try {
            sessionManager.clear()
            true
        } catch (e: Exception) {
            false
        }
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