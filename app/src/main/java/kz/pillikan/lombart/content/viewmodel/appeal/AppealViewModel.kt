package kz.pillikan.lombart.content.viewmodel.appeal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.SignInRepository
import kz.pillikan.lombart.content.model.repository.appeal.AppealRepository
import kz.pillikan.lombart.content.model.request.appeal.FeedbackRequest

class AppealViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AppealRepository = AppealRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    fun sendFeedback(feedbackRequest: FeedbackRequest) {
        viewModelScope.launch {
            try {
                when (repository.sendFeedback(feedbackRequest)) {
                    true -> isSuccess.postValue(true)
                    false -> isSuccess.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}