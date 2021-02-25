package kz.pillikan.lombart.common.remote

import kz.pillikan.lombart.authorization.model.request.*
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.authorization.model.response.SignInResponse
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.model.response.home.ProfileResponse
import kz.pillikan.lombart.content.model.response.home.ServiceCurrencyResponse
import kz.pillikan.lombart.content.model.response.home.TicketsResponse
import kz.pillikan.lombart.content.model.response.home.WeatherResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface NetworkService {

    @POST(EndPoints.POST_SIGN_IN)
    suspend fun logIn(
        @Header("appVer") appVer: String,
        @Body signInRequest: SignInRequest
    ): Response<SignInResponse>

    @POST(EndPoints.POST_SIGN_UP)
    suspend fun createUser(
        @Header("appVer") appVer: String,
        @Body signUpRequest: SignUpRequest
    ): Response<SignInResponse>

    @POST(EndPoints.POST_CHECK_USER)
    suspend fun checkUser(
        @Header("appVer") appVer: String,
        @Body checkUserRequest: CheckUserRequest
    ): Response<CheckResponse>

    @POST(EndPoints.POST_SMS)
    suspend fun sendSms(
        @Header("appVer") appVer: String,
        @Body sendSmsRequest: SendSmsRequest
    ): Response<ResponseBody>

    @POST(EndPoints.POST_VERIFICATION)
    suspend fun verificationNumber(
        @Header("appVer") appVer: String,
        @Body checkNumberRequest: CheckNumberRequest
    ): Response<ResponseBody>

    @POST(EndPoints.POST_PIN)
    suspend fun pinCode(
        @Header("appVer") appVer: String,
        @Header("Authorization") Authorization: String,
        @Body pinCodeRequest: PinCodeRequest
    ): Response<ResponseBody>

    @POST(EndPoints.POST_TICKET)
    suspend fun ticketsList(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<TicketsResponse>

    @POST(EndPoints.POST_SERVICE_CURRENCY)
    suspend fun serviceCurrency(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<ServiceCurrencyResponse>

    @POST(EndPoints.POST_GET_WEATHER)
    suspend fun getWeather(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<WeatherResponse>

    @POST(EndPoints.POST_FEEDBACK)
    suspend fun sendFeedback(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body feedbackRequest: FeedbackRequest
    ): Response<ResponseBody>

    @POST(EndPoints.POST_PROFILE_INFO)
    suspend fun profileInfo(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<ProfileResponse>

    @POST(EndPoints.POST_RESET_PASSWORD)
    suspend fun resetPassword(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ResponseBody>

}