package kz.pillikan.lombart.common.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Networking {

    fun create(baseUrl: String): NetworkService {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttp: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(logger)

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttp.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NetworkService::class.java)
    }

}