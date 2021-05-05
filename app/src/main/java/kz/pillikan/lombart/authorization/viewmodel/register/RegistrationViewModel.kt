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
import kz.pillikan.lombart.common.remote.Constants

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: RegistrationRepository = RegistrationRepository(application)

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    val isError: MutableLiveData<String> = MutableLiveData()
    val isCheckUser: MutableLiveData<CheckResponse> = MutableLiveData()

    suspend fun checkUser(checkUserRequest: CheckUserRequest) {
        viewModelScope.launch {
            try {
                val response = repository.checkUser(checkUserRequest)
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

    fun clearSharedPref() {
        try {
            repository.clearSharedPref()
        } catch (e: Exception) {
        }
    }

}