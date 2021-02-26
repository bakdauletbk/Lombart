package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.CreatePasswordRepository
import kz.pillikan.lombart.authorization.model.request.SignUpRequest

class CreatePasswordViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "CreatePasswordViewModel"
    }

    private var repository: CreatePasswordRepository = CreatePasswordRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    suspend fun createUser(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            try {
                when (repository.createUser(signUpRequest)) {
                    true -> isSuccess.postValue(true)
                    false -> isSuccess.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

}