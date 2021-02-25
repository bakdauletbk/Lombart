package kz.pillikan.lombart.common.helpers

import java.util.*
import java.util.regex.Pattern

object Validators {

    private const val MIN_PASSWORD_LENGTH = 6
    private const val MIN_PINCODE_LENGTH = 3
    private const val MIN_PHONE_LENGTH = 18
    private const val INN_LENGTH = 11
    private val CORRECT_LOGIN = Pattern.compile(
        "[0-9\\+\\.\\_\\%\\-\\+]{1,256}"
    )

    fun validateIsNotEmpty(text: String): Boolean {
        return when {
            text.isBlank() -> false
            else -> true
        }
    }

    fun validateInn(inn: String): Boolean {
        return when {
            inn.isBlank() -> false
            inn.length <= INN_LENGTH -> false
            else -> true
        }
    }

    fun validatePinCode(pincode: String): Boolean {
        return when {
            pincode.isBlank() -> false
            pincode.length <= MIN_PINCODE_LENGTH -> false
            else -> true
        }
    }

    fun validatePassword(password: String): Boolean {
        return when {
            password.isBlank() -> false
            password.length < MIN_PASSWORD_LENGTH -> false
            else -> true
        }
    }

    fun validatePhoneSize(phone: String): Boolean {
        return when {
            phone.isBlank() -> false
            phone.length > MIN_PHONE_LENGTH -> false
            else -> true
        }
    }

    fun validatePhone(phone: String): Boolean {
        val phoneCode: String = phone.substring(4, 7)
        val series = Arrays.asList(
            "700",
            "701",
            "702",
            "705",
            "707",
            "708",
            "747",
            "775",
            "776",
            "778",
            "771",
            "777"
        )
        return series.contains(phoneCode)
    }
}