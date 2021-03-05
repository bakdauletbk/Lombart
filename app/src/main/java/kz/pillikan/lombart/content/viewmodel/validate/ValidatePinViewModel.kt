package kz.pillikan.lombart.content.viewmodel.validate

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.validate.ValidatePinRepository
import kz.pillikan.lombart.content.model.request.home.ValidatePinRequest

class ValidatePinViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: ValidatePinRepository = ValidatePinRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    fun validatePin(validatePinRequest: ValidatePinRequest) {
        viewModelScope.launch {
            try {
                val isValidate = repository.validatePin(validatePinRequest)
                isSuccess.postValue(isValidate)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}