package kz.pillikan.lombart.splash

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.common.helpers.NetworkHelpers
import kz.pillikan.lombart.common.preference.SessionManager
import java.lang.Exception

class SplashRepository(application: Application) {

    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager = SessionManager(sharedPreferences)

    fun checkNetwork(context: Context): Boolean {
        return NetworkHelpers.isNetworkConection(context)
    }

    fun checkAuthorize(): Boolean? {
        return sessionManager.getIsAuthorize()
    }

}