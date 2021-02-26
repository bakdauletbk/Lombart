package kz.pillikan.lombart.authorization.viewmodel.pin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.pin.PinCodeRepository
import kz.pillikan.lombart.authorization.model.request.PinCodeRequest
import java.lang.Exception

class PinCodeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "PinCodeViewModel"
    }

    private var repository: PinCodeRepository = PinCodeRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    suspend fun savePinCode(pinCodeRequest: PinCodeRequest) {
        viewModelScope.launch {
            try {
                when (repository.savePinCode(pinCodeRequest)) {
                    true -> isSuccess.postValue(true)
                    false -> isSuccess.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

}