package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kz.pillikan.lombart.authorization.model.repository.registration.SignInRepository
import kz.pillikan.lombart.authorization.model.request.SignInRequest
import kz.pillikan.lombart.common.remote.Constants
import java.lang.Exception

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "SignInViewModel"
    }

    private var repository: SignInRepository = SignInRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val firebaseToken: MutableLiveData<String> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    fun createFMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(
                    CreatePasswordViewModel.TAG,
                    "Fetching FCM registration token failed",
                    task.exception
                )
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = token.toString()
            Log.d(CreatePasswordViewModel.TAG, msg)
            firebaseToken.postValue(msg)
        })
    }

    suspend fun signIn(signInRequest: SignInRequest) {
        viewModelScope.launch {
            try {
                val response = repository.signIn(signInRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> {
                        repository.saveUser(response.body()!!.user)
                        isSuccess.postValue(true)
                    }
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isSuccess.postValue(false)
                }
            } catch (e: Exception) {
                Log.d(TAG, e.message.toString())
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