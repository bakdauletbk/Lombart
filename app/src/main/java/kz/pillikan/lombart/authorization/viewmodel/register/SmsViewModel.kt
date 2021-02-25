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

    fun verificationSms(checkNumberRequest: CheckNumberRequest) {
        viewModelScope.launch {
            try {
                when (repository.verificationSms(checkNumberRequest)) {
                    true -> isVerificationNumber.postValue(true)
                    false -> isVerificationNumber.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

    fun sendSms(sendSmsRequest: SendSmsRequest) {
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