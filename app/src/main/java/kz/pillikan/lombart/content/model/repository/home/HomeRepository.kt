package kz.pillikan.lombart.content.model.repository.home

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.response.home.*

class HomeRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    fun getLanguage(): String = sessionManager.getLanguage().toString()

    fun setLanguage(language: String): Boolean {
        return try {
            sessionManager.setLanguage(language)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getLoans(): ArrayList<Tickets>? {
        val response = networkService.ticketsList(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()?.tickets
        } else {
            null
        }
    }

    suspend fun getCurrency(): ArrayList<CurrencyList>? {
        val response =
            networkService.serviceCurrency(
                Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
                BuildConfig.VERSION_NAME
            )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()?.currency
        } else {
            null
        }
    }

    suspend fun getWeather(): WeatherData? {
        val response = networkService.getWeather(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()?.data
        } else {
            null
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

    suspend fun getSliderList(): ArrayList<SlidersList>? {
        val response = networkService.sliderList(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()!!.sliders
        } else {
            null
        }
    }

    suspend fun getFinenessPrice(): FinenessPriceResponse? {
        val response = networkService.finenessPrice(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getHeadText(): TitleResponse? {
        val response = networkService.getHeadText(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()
        } else {
            null
        }
    }

}