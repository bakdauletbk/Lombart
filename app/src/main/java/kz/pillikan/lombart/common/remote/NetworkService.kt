package kz.pillikan.lombart.common.remote

import kz.pillikan.lombart.authorization.model.request.*
import kz.pillikan.lombart.authorization.model.response.CheckResponse
import kz.pillikan.lombart.authorization.model.response.SignInResponse
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.model.request.home.PayRequest
import kz.pillikan.lombart.content.model.request.home.ValidatePinRequest
import kz.pillikan.lombart.content.model.request.notifications.PageRequest
import kz.pillikan.lombart.content.model.request.profile.CardRequest
import kz.pillikan.lombart.content.model.request.profile.ChangePassword
import kz.pillikan.lombart.content.model.request.profile.CheckPassword
import kz.pillikan.lombart.content.model.response.about.AboutResponse
import kz.pillikan.lombart.content.model.response.about.AddressResponse
import kz.pillikan.lombart.content.model.response.appeal.ResponseAdvancedData
import kz.pillikan.lombart.content.model.response.home.*
import kz.pillikan.lombart.content.model.response.notifications.NotificationsResponse
import kz.pillikan.lombart.content.model.response.profile.CardResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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
    ): Response<CurrencyResponse>

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
        @Header("appVer") appVer: String,
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<ResponseBody>

    @POST(EndPoints.POST_SLIDERS)
    suspend fun sliderList(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<SlidersResponse>

    @POST(EndPoints.POST_NOTIFICATION)
    suspend fun notificationList(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body pageRequest: PageRequest
    ): Response<NotificationsResponse>

    @POST(EndPoints.POST_FINENESS_PRICE)
    suspend fun finenessPrice(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<FinenessPriceResponse>

    @POST(EndPoints.POST_VALIDATE_PIN)
    suspend fun validatePin(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body validatePinRequest: ValidatePinRequest
    ): Response<ResponseBody>

    @POST(EndPoints.POST_ADDRESS)
    suspend fun getAddress(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<AddressResponse>

    @POST(EndPoints.POST_CHECK_PASSWORD)
    suspend fun checkPassword(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body checkPassword: CheckPassword
    ): Response<ResponseBody>

    @POST(EndPoints.POST_CHANGE_PASSWORD)
    suspend fun changePassword(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body changePassword: ChangePassword
    ): Response<ResponseBody>

    @POST(EndPoints.POST_GET_HEAD)
    suspend fun getHeadText(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<TitleResponse>

    @POST(EndPoints.POST_ABOUT)
    suspend fun getAbout(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<AboutResponse>

    @POST(EndPoints.POST_ADVANCED_DATA)
    suspend fun getAdvanceData(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<ResponseAdvancedData>

    @POST(EndPoints.POST_ADD_CARD)
    suspend fun addCard(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body cardRequest: CardRequest
    ): Response<CardResponse>

    @GET(EndPoints.GET_CARD)
    suspend fun getCardList(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String
    ): Response<CardListResponse>

    @POST(EndPoints.POST_PAY)
    suspend fun pay(
        @Header("Authorization") Authorization: String,
        @Header("appVer") appVer: String,
        @Body payRequest: PayRequest
    ): Response<ResponseBody>

}