package kz.pillikan.lombart

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import kz.pillikan.lombart.common.remote.ApiConstants

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initYandexMapKit()
    }

    private fun initYandexMapKit() {
        MapKitFactory.setApiKey(ApiConstants.YANDEX_API_KEY)
    }
}