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
import kz.pillikan.lombart.common.remote.Constants

class PasswordRecoveryViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: PasswordRecoveryRepository = PasswordRecoveryRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val isCheckUser: MutableLiveData<CheckResponse> = MutableLiveData()
    val isSendSms: MutableLiveData<Boolean> = MutableLiveData()
    val isVerificationNumber: MutableLiveData<Boolean> = MutableLiveData()
    val isResetPassword: MutableLiveData<Boolean> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun checkUser(checkUserRequest: CheckUserRequest) {
        viewModelScope.launch {
            try {
                val response = repository.checkUser(checkUserRequest = checkUserRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isCheckUser.postValue(response.body())
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
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
                isError.postValue(null)
            }
        }
    }

    suspend fun verificationNumber(checkNumberRequest: CheckNumberRequest) {
        viewModelScope.launch {
            try {
                val response = repository.verificationNumber(checkNumberRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isVerificationNumber.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isVerificationNumber.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest) {
        viewModelScope.launch {
            try {
                val response = repository.resetPassword(resetPasswordRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isResetPassword.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isResetPassword.postValue(false)
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