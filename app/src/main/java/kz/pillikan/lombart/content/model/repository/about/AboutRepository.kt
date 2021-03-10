package kz.pillikan.lombart.content.model.repository.about

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.response.about.AboutResponse
import kz.pillikan.lombart.content.model.response.about.AddressList

class AboutRepository(application: Application) {

    private val networkService =
        Networking.create(Constants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun getAddress(): ArrayList<AddressList>? {
        val response = networkService.getAddress(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE) {
            response.body()?.data
        } else {
            null
        }
    }

    suspend fun getAbout(): AboutResponse? {
        val response = networkService.getAbout(
            Constants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
            BuildConfig.VERSION_NAME
        )
        return if (response.code() == Constants.RESPONSE_SUCCESS_CODE){
            response.body()
        }else{
            null
        }
    }

}