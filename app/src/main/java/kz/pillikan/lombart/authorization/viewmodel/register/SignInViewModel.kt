package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.SignInRepository
import kz.pillikan.lombart.authorization.model.request.SignInRequest
import java.lang.Exception

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "SignInViewModel"
    }

    private var repository: SignInRepository = SignInRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    suspend fun signIn(signInRequest: SignInRequest) {
        viewModelScope.launch {
            try {
                isSuccess.postValue(repository.signIn(signInRequest))
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
                isError.postValue(e.toString())
            }
        }
    }

}