package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.CreatePasswordRepository
import kz.pillikan.lombart.authorization.model.request.SignUpRequest
import kz.pillikan.lombart.common.remote.Constants

class CreatePasswordViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "CreatePasswordViewModel"
    }

    private var repository: CreatePasswordRepository = CreatePasswordRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val firebaseToken: MutableLiveData<String> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    fun createFMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = token.toString()
            firebaseToken.postValue(msg)
        })
    }

    suspend fun createUser(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            try {
                val response = repository.createUser(signUpRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> {
                        isSuccess.postValue(true)
                        repository.saveUser(response.body()!!)
                    }
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