package kz.pillikan.lombart.authorization.viewmodel.recovery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.recovery.PasswordRecoveryRepository
import kz.pillikan.lombart.authorization.model.request.CheckNumberRequest
import kz.pillikan.lombart.authorization.model.request.CheckUserRequest
import kz.pillikan.lombart.authorization.model.request.ResetPasswordRequest
import kz.pillikan.lombart.authorization.model.request.SendSmsRequest
import kz.pillikan.lombart.authorization.model.response.CheckResponse

class PasswordRecoveryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PasswordRecoveryRepository = PasswordRecoveryRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val isCheckUser: MutableLiveData<CheckResponse> = MutableLiveData()
    val isSendSms: MutableLiveData<Boolean> = MutableLiveData()
    val isVerificationNumber: MutableLiveData<Boolean> = MutableLiveData()
    val isResetPassword: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun checkUser(checkUserRequest: CheckUserRequest) {
        viewModelScope.launch {
            try {
                val response = repository.checkUser(checkUserRequest = checkUserRequest)
                if (response != null) {
                    isCheckUser.postValue(response)
                } else {
                    isCheckUser.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun sendSms(sendSmsRequest: SendSmsRequest) {
        viewModelScope.launch {
            try {
                when (val isSend = repository.sendSms(sendSmsRequest)) {
                    true -> isSendSms.postValue(true)
                    false -> isSendSms.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun verificationNumber(checkNumberRequest: CheckNumberRequest) {
        viewModelScope.launch {
            try {
                when (val isSend = repository.verificationNumber(checkNumberRequest)) {
                    true -> isVerificationNumber.postValue(true)
                    false -> isVerificationNumber.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        viewModelScope.launch {
            try {
                when (val isReset = repository.resetPassword(resetPasswordRequest)) {
                    true -> isResetPassword.postValue(true)
                    false -> isResetPassword.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }
}