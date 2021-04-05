package kz.pillikan.lombart.content.viewmodel.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.models.Page
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.notifications.NotificationsRepository
import kz.pillikan.lombart.content.model.response.notifications.DataList

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    val notificationList: MutableLiveData<List<DataList>> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    private val repository: NotificationsRepository = NotificationsRepository(application)

    private var notificationsPage: Page<DataList>? = null
    private var isLoading = true

    fun getInitialPage() {
        loadPage(Constants.ZERO)
    }

    fun getNextPage() {
        viewModelScope.launch {
            val nextPage =
                if (notificationsPage != null) notificationsPage!!.getPageNumber()
                    .plus(Constants.ONE) else Constants.ZERO
            loadPage(nextPage)
        }
    }

    fun isHasNext(): Boolean {
        return notificationsPage != null && notificationsPage!!.hasNextPage()
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
                    isLoading = false
                    notificationsPage = response
                    if (notificationsPage!!.getContent()!!.isNullOrEmpty()) {
                        notificationList.postValue(null)
                    } else {
                        notificationList.postValue(notificationsPage!!.getContent())
                    }
                } else {
                    isLoading = false
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }


}