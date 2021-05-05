package kz.pillikan.lombart.authorization.viewmodel.pin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.pin.PinCodeRepository
import kz.pillikan.lombart.authorization.model.request.PinCodeRequest
import kz.pillikan.lombart.common.remote.Constants
import java.lang.Exception

class PinCodeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "PinCodeViewModel"
    }

    private var repository: PinCodeRepository = PinCodeRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun savePinCode(pinCodeRequest: PinCodeRequest) {
        viewModelScope.launch {
            try {
                val response = repository.savePinCode(pinCodeRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isSuccess.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isSuccess.postValue(false)
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