package kz.pillikan.lombart.common.remote

object EndPoints {
    //Авторизация
    const val POST_SIGN_IN = "/v1/auth/sign-in"
    const val POST_SIGN_UP = "/v1/auth/sign-up"
    const val POST_CHECK_USER = "/v1/auth/check-user"
    const val POST_VERIFICATION = "/v1/auth/verification"
    const val POST_SMS = "/v1/auth/sms"
    const val POST_PIN = "/v1/profile/pin"
    const val POST_TICKET = "/v1/cert/get-tickets"
    const val POST_SERVICE_CURRENCY = "/v1/service/get-currency"
    const val POST_GET_WEATHER = "/v1/service/get-weather"
    const val POST_FEEDBACK = "/v1/feedback/send"
    const val POST_PROFILE_INFO = "/v1/profile/info"
    const val POST_RESET_PASSWORD = "/v1/auth/reset"
    const val POST_SLIDERS = "/v1/slider/list"
    const val POST_NOTIFICATION = "/v1/notification/list"
}