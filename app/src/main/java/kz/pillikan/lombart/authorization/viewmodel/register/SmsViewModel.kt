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
import kz.pillikan.lombart.common.remote.Constants

class SmsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "SmsViewModel"
    }

    private var repository: SmsRepository = SmsRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val isVerificationNumber: MutableLiveData<Boolean> = MutableLiveData()
    val isSendSms: MutableLiveData<Boolean> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun verificationSms(checkNumberRequest: CheckNumberRequest) {
        viewModelScope.launch {
            try {
                val response = repository.verificationSms(checkNumberRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isVerificationNumber.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isVerificationNumber.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

    suspend fun sendSms(sendSmsRequest: SendSmsRequest) {
        viewModelScope.launch {
            try {
                val response = repository.sendSms(sendSmsRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isSendSms.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isSendSms.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(e.toString())
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