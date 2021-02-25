package kz.pillikan.lombart.common.preference

import android.content.SharedPreferences

class SessionManager(private val pref: SharedPreferences) {

    companion object {
        const val KEY_IS_AUTHORIZE = "KEY_IS_AUTHORIZE"
        const val KEY_INN = "KEY_INN"
        const val KEY_PHONE = "KEY_PHONE"
        const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
    }

    fun getIsAuthorize(): Boolean? = pref.getBoolean(KEY_IS_AUTHORIZE, false)
    fun setIsAuthorize(isAuthorize: Boolean) {
        pref.edit().putBoolean(KEY_IS_AUTHORIZE, isAuthorize).apply()
    }

    fun getInn(): String? = pref.getString(KEY_INN, null)
    fun setInn(iin: String) {
        pref.edit().putString(KEY_INN, iin).apply()
    }

    fun getPhone(): String? = pref.getString(KEY_PHONE, null)
    fun setPhone(phone: String) {
        pref.edit().putString(KEY_PHONE, phone).apply()
    }


    fun getToken(): String? = pref.getString(KEY_ACCESS_TOKEN, null)
    fun setToken(token: String) {
        pref.edit().putString(KEY_ACCESS_TOKEN, token).apply()
    }

    fun clear() {
        pref.edit().clear().apply()
    }

}