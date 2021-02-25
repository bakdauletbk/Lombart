package kz.pillikan.lombart.content.viewmodel.notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.notifications.NotificationsRepository
import kz.pillikan.lombart.content.model.response.notifications.NotificationsModel

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {

    var notificationList: MutableLiveData<List<NotificationsModel>> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    private val repository: NotificationsRepository = NotificationsRepository(application)

    fun getNotifications() {
        viewModelScope.launch {
            try {
                val response = repository.getNotifications()
                notificationList.postValue(response as List<NotificationsModel>?)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }


}