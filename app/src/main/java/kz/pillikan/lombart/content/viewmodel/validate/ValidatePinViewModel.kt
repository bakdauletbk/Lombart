package kz.pillikan.lombart.content.viewmodel.validate

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.validate.ValidatePinRepository
import kz.pillikan.lombart.content.model.request.home.ValidatePinRequest

class ValidatePinViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ValidatePinRepository = ValidatePinRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val isNetworkConnection: MutableLiveData<Boolean> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun validatePin(validatePinRequest: ValidatePinRequest) {
        viewModelScope.launch {
            try {
                val response = repository.validatePin(validatePinRequest)
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

    suspend fun checkNetwork(context: Context) {
        viewModelScope.launch {
            try {
                isNetworkConnection.postValue(repository.checkNetwork(context))
            } catch (e: java.lang.Exception) {
                isNetworkConnection.postValue(false)
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