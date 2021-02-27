package kz.pillikan.lombart.content.model.repository.notifications

import android.app.Application
import android.content.Context
import android.util.Base64
import kz.pillikan.lombart.BuildConfig
import kz.pillikan.lombart.common.helpers.base64encode
import kz.pillikan.lombart.common.models.Page
import kz.pillikan.lombart.common.preference.SessionManager
import kz.pillikan.lombart.common.remote.ApiConstants
import kz.pillikan.lombart.common.remote.Networking
import kz.pillikan.lombart.content.model.request.notifications.PageRequest
import kz.pillikan.lombart.content.model.response.notifications.DataList
import kz.pillikan.lombart.content.model.response.notifications.NotificationsResponse

class NotificationsRepository(application: Application) {

    companion object {
        const val PAGE_LIMIT = "10"
    }

    private val networkService =
        Networking.create(ApiConstants.BASE_URL)
    private var sharedPreferences =
        application.getSharedPreferences("sessionManager", Context.MODE_PRIVATE)
    private var sessionManager: SessionManager =
        SessionManager(sharedPreferences)

    suspend fun getNotifications(page: Int): Page<DataList>? {
        val limitBase64 = base64encode(PAGE_LIMIT)
        val pageBase64 = base64encode(page.toString())
        val pageRequest = PageRequest(limit = limitBase64, page = pageBase64)
        val response =
            networkService.notificationList(
                Authorization = ApiConstants.AUTH_TOKEN_PREFIX + sessionManager.getToken(),
                appVer = BuildConfig.VERSION_NAME,
                pageRequest
            )
        return if (response.code() == ApiConstants.RESPONSE_SUCCESS_CODE) {
            val pageNumber = response.body()?.pages!!.toInt()
            val hasNextPage = !response.body()?.hasNext!!
            val notifications: ArrayList<DataList> = ArrayList()
            for (i in response.body()?.data?.indices!!) {
                notifications.add(response.body()!!.data[i])
            }
            val notificationPage: Page<DataList>? =
                Page(notifications, pageNumber, hasNextPage)

            notificationPage
        } else {
            null
        }
    }

}