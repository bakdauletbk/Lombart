package kz.pillikan.lombart.common.remote

object Constants {
    const val BASE_URL = "https://api-cert.smartideagroup.kz"
    const val IMG_BASE_URL = "https://st-cert.smartideagroup.kz"
    const val IMG_SLIDER_URL = "/images/slider/"
    const val IMG_URL_ABOUT = "/images/material/"
    const val RESPONSE_SUCCESS_CODE = 200
    const val AUTH_TOKEN_PREFIX = "Bearer "
    const val YANDEX_API_KEY = "704ca01a-a014-47f8-9d6c-a926b47b6d2b"

    //----------[Константы для вызова алерта]
    const val ALERT_TYPE_LOAN_DETAILS = 0
    const val ALERT_TYPE_SUCCESS = 1
    const val ZERO = 0

    //----------[Константы для Контента]
    const val MONEY = " тг"
    const val YEARS = "  г."
    const val NUMBERING = "№ "
    const val CLEAR_SKY = "01d"
    const val FEW_CLOUDS = "02d"
    const val SCATTERED_CLOUDS = "03d"
    const val BROKEN_CLOUDS = "04d"
    const val SHOWER_RAIN = "09d"
    const val RAIN = "10d"
    const val THUNDERSTORM = "11d"
    const val SNOW = "13d"
    const val MIST = "50d"
    const val USA_CURRENCY = 0
    const val EUROPA_CURRENCY = 1
    const val RUSSIA_CURRENCY = 2
    const val PADDING = 20f
    const val PADDING_TOP = 0
    const val PADDING_BOTTOM = 0
    const val PAGE_MARGIN = 12f
    const val FINENESS_FIRST = 0
    const val FINENESS_SECOND = 1
    const val FINENESS_THIRD = 2
    const val DAYS = 30
    const val FINENESS = "Проба AU "
    const val EMPTY = ""
    const val CELSIUS = " ºC"
    const val ONE = 1
    const val TIME_MILLIS: Long = 4000
    const val NUMBER_TRANSACTION = "Номер транзакции: "
    const val PRICE = " , Сумма: "
    const val DATE_TRANSACTION = " , Дата транзакции: "
    const val STATUS_PAY = " , Статус: Оплачено"
    const val FORMAT_DATE = "dd.MM.yyyy"
    const val FOR = "за "
    const val TEL = "tel: "

    //Validate Days
    const val MIN_DAY = 1
    const val MAX_DAY = 45
    const val MAX_DAY_THIRTY = 30

    const val ONE_HUNDRED = 100
    const val ONE_THOUSAND = 1000

    const val KAZ = "kk"
    const val RUS = "ru"

    //Menu item
    const val HOME = 0
    const val ABOUT = 1
    const val PROFILE = 2
    const val NOTIFICATION = 3
    const val APPEAL = 4

    //Uri
    const val WHATSAPP_URI = "http://api.whatsapp.com/send?phone="
}