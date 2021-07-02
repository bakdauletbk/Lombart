package kz.pillikan.lombart.common.remote

object EndPoints {

    //Авторизация
    const val POST_SIGN_IN = "/v1/auth/sign-in"
    const val POST_SIGN_UP = "/v1/auth/sign-up"
    const val POST_CHECK_USER = "/v1/auth/check-user"
    const val POST_VERIFICATION = "/v1/auth/verification"
    const val POST_SMS = "/v1/auth/sms"
    const val POST_PIN = "/v1/profile/set-pin"
    const val POST_RESET_PASSWORD = "/v1/auth/reset"

    //Content
    const val POST_TICKET = "/v1/cert/get-tickets"
    const val POST_SERVICE_CURRENCY = "/v1/service/get-currency"
    const val POST_GET_WEATHER = "/v1/service/get-weather"
    const val POST_FEEDBACK = "/v1/feedback/send"
    const val POST_PROFILE_INFO = "/v1/profile/info"
    const val POST_SLIDERS = "/v1/slider/list"
    const val POST_NOTIFICATION = "/v1/notification/list"
    const val POST_FINENESS_PRICE = "/v1/cert/get-prices"
    const val POST_VALIDATE_PIN = "/v1/profile/validate-pin"
    const val POST_ADDRESS = "/v1/cert/addresses-contacts"
    const val POST_CHECK_PASSWORD = "/v1/profile/check-password"
    const val POST_CHANGE_PASSWORD = "/v1/profile/change-password"
    const val POST_GET_HEAD = "/v1/service/get-head"
    const val POST_ABOUT = "/v1/service/get-about"
    const val POST_ADVANCED_DATA = "/v1/service/get-advanced-data"
    const val POST_ADD_CARD = "/v1/pay/add-card"
    const val GET_CARD = "v1/pay/card-list"
    const val POST_DELETE_CARD = "v1/pay/delete-card"
    const val POST_PAY = "/v1/pay/pay"
}