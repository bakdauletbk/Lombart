package kz.pillikan.lombart.content.model

import android.app.Application
import android.content.Context
import kz.pillikan.lombart.common.preference.SessionManager

class FoundationRepository(application: Application) {

    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    fun setLanguage(language: String): Boolean {
        return try {
            sessionManager.setLanguage(language)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun language(): String? = sessionManager.getLanguage().toString()

    fun getIsFirstLanguage(): Boolean = sessionManager.getIsFirstLanguage()!!

    fun setIsFirstLanguage(): Boolean {
        return try {
            sessionManager.setIsFirstLanguage(true)
            true
        } catch (e: Exception) {
            false
        }
    }

}