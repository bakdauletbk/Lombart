package kz.pillikan.lombart.content.viewmodel.appeal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.SignInRepository
import kz.pillikan.lombart.content.model.repository.appeal.AppealRepository
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest
import kz.pillikan.lombart.content.model.response.appeal.ResponseAdvancedData

class AppealViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AppealRepository = AppealRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val advancedData = MutableLiveData<ResponseAdvancedData>()

    suspend fun sendFeedback(feedbackRequest: FeedbackRequest) {
        viewModelScope.launch {
            try {
                isSuccess.postValue(repository.sendFeedback(feedbackRequest))
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getAdvancedData() {
        viewModelScope.launch {
            try {
                advancedData.postValue(repository.getAdvancedData())
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }
}