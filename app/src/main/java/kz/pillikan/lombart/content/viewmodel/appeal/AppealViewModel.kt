package kz.pillikan.lombart.content.viewmodel.appeal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.appeal.AppealRepository
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.model.response.appeal.ResponseAdvancedData

class AppealViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AppealRepository = AppealRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val advancedData = MutableLiveData<ResponseAdvancedData>()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun sendFeedback(feedbackRequest: FeedbackRequest) {
        viewModelScope.launch {
            try {
                val response = repository.sendFeedback(feedbackRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isSuccess.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isSuccess.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getAdvancedData() {
        viewModelScope.launch {
            try {
                val response = repository.getAdvancedData()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> advancedData.postValue(response.body())
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    fun clearSharedPref() {
        try {
            repository.clearSharedPref()
        } catch (e: Exception) {
        }
    }
}