package kz.pillikan.lombart.content.model.repository.profile

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.profile.DeleteCardRequest
import kz.pillikan.lombart.content.model.response.home.CardListResponse
import kz.pillikan.lombart.content.model.response.home.ProfileInfo
import kz.pillikan.lombart.content.model.response.home.ProfileResponse
import kz.pillikan.lombart.content.model.response.profile.CardModel
import okhttp3.ResponseBody
import retrofit2.Response

class ProfileRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

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

    suspend fun removeCard(deleteCardRequest: DeleteCardRequest): Response<ResponseBody> {
        return networkService.deleteCard(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME,
            deleteCardRequest
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