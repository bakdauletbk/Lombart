package kz.pillikan.lombart.content.viewmodel.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.models.Page
import kz.pillikan.lombart.content.model.repository.notifications.NotificationsRepository
import kz.pillikan.lombart.content.model.response.notifications.DataList
import kz.pillikan.lombart.content.model.response.notifications.NotificationsModel

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    var notificationList: MutableLiveData<List<DataList>> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    private val repository: NotificationsRepository = NotificationsRepository(application)

    private var notificationsPage: Page<DataList>? = null
    private var isLoading = false

    fun getInitialPage() {
        loadPage(0)
    }

    fun getNextPage() {
        val nextPage = if (notificationsPage != null) notificationsPage!!.getPageNumber() + 1 else 0
        loadPage(nextPage)
    }

    fun isLastPage(): Boolean {
        return notificationsPage != null && !notificationsPage!!.hasNextPage()
    }

    fun isLoading(): Boolean {
        return isLoading
    }

    private fun loadPage(page: Int) {
        viewModelScope.launch {
            try {
                isLoading = true
                val response = repository.getNotifications(page)
                if (response != null) {
                    notificationsPage = response
                    if (notificationsPage!!.getContent().isNullOrEmpty()) {
                        notificationList.postValue(null)
                    } else {
                        notificationList.postValue(notificationsPage!!.getContent())
                    }
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }


}