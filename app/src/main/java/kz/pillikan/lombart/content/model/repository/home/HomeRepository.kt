package kz.pillikan.lombart.content.model.repository.home

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.home.PayRequest
import kz.pillikan.lombart.content.model.response.home.*
import okhttp3.ResponseBody
import retrofit2.Response

class HomeRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    fun clearSharedPref(): Boolean {
        return try {
            sessionManager.clear()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getLanguage(): String = sessionManager.getLanguage().toString()

    fun setLanguage(language: String): Boolean {
        return try {
            sessionManager.setLanguage(language)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getLoans(): Response<TicketsResponse> {
        return networkService.ticketsList(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getCurrency(): Response<CurrencyResponse> {
        return networkService.serviceCurrency(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getWeather(): Response<WeatherResponse> {
        return networkService.getWeather(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getProfile(): Response<ProfileResponse> {
        return networkService.profileInfo(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getSliderList(): Response<SlidersResponse> {
        return networkService.sliderList(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getFinenessPrice(): Response<FinenessPriceResponse> {
        return networkService.finenessPrice(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getHeadText(): Response<TitleResponse> {
        return networkService.getHeadText(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
    }

    suspend fun getCards(): Response<CardListResponse> = networkService.getCardList(
        Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
        BuildConfig.VERSION_NAME
    )

    suspend fun payLoans(payRequest: PayRequest): Response<ResponseBody> = networkService.pay(
        Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
        BuildConfig.VERSION_NAME,
        payRequest
    )

}