package kz.pillikan.lombart.content.model.repository.home

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.response.home.*

class HomeRepository(application: Application) {

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun getLoans(): ArrayList<Tickets>? {
        val response = networkService.ticketsList(
            ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            response.body()?.tickets
        } else {
            null
        }
    }

    suspend fun getCurrency(): ArrayList<CurrencyList>? {
        val response =
            networkService.serviceCurrency(
                ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
                BuildConfig.VERSION_NAME
            )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            response.body()?.currency
        } else {
            null
        }
    }

    suspend fun getWeather(): WeatherData? {
        val response = networkService.getWeather(
            ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            response.body()?.data
        } else {
            null
        }
    }

    suspend fun getProfile(): ProfileInfo? {
        val response = networkService.profileInfo(
            ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            response.body()?.profile
        } else {
            null
        }
    }

    suspend fun getSlidersList(): ArrayList<SlidersList>? {
        val response = networkService.slidersList(
            ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            response.body()!!.sliders
        } else {
            null
        }
    }

}