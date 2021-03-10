package kz.pillikan.lombart.common.helpers

import android.content.res.Resources
import android.util.Base64

//Base64 encode
fun base64encode(decode: String): String {
    return Base64.encodeToString(decode.toByteArray(), Base64.NO_WRAP)
}

fun convertDpToPixel(dp: Float): Int {
    val scale = Resources.getSystem().displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun formatDate(date: String): String {
    val charArray: CharArray = date.toCharArray()
    return date.take(2) + "." + charArray[3] + charArray[4] + "." + date.takeLast(4)
}

fun formatDateTime(date: String): String {
    val charArray: CharArray = date.toCharArray()
    return charArray[8] + "" + charArray[9] + "." + charArray[5] + charArray[6] + "." + date.take(4)
}

fun convertSms(sms : String):String{
    return sms.split(":".toRegex()).toTypedArray()[1].trim { it <= ' ' }
}
