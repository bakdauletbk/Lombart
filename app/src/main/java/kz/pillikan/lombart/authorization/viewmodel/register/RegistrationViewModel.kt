package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.RegistrationRepository
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: RegistrationRepository = RegistrationRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val isCheckUser: MutableLiveData<CheckResponse> = MutableLiveData()
    val isSendSms: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun checkUser(checkUserRequest: CheckUserRequest) {
        viewModelScope.launch {
            try {
                val response = repository.checkUser(checkUserRequest)
                isCheckUser.postValue(response)
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

    suspend fun sendSms(sendSmsRequest: SendSmsRequest) {
        viewModelScope.launch {
            try {
                when (repository.sendSms(sendSmsRequest)) {
                    true -> isSendSms.postValue(true)
                    false -> isSendSms.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

}