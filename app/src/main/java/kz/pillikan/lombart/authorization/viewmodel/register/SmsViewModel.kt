package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.SmsRepository
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest

class SmsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "SmsViewModel"
    }

    private var repository: SmsRepository = SmsRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val isVerificationNumber: MutableLiveData<Boolean> = MutableLiveData()
    val isSendSms: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun verificationSms(checkNumberRequest: CheckNumberRequest) {
        viewModelScope.launch {
            try {
                isVerificationNumber.postValue(repository.verificationSms(checkNumberRequest))
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

    suspend fun sendSms(sendSmsRequest: SendSmsRequest) {
        viewModelScope.launch {
            try {
                isSendSms.postValue(repository.sendSms(sendSmsRequest))
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }


}