package kz.pillikan.lombart.authorization.viewmodel.register

import android.app.Application
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import kz.pillikan.lombart.R
import kz.pillikan.lombart.authorization.model.repository.registration.CreatePasswordRepository
import kz.pillikan.lombart.authorization.model.request.SignUpRequest

class CreatePasswordViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "CreatePasswordViewModel"
    }

    private var repository: CreatePasswordRepository = CreatePasswordRepository(application)

    val isSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val firebaseToken: MutableLiveData<String> = MutableLiveData()


    fun createFMToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg =  token.toString()
            Log.d(TAG, msg)
            firebaseToken.postValue(msg)
        })
    }

    suspend fun createUser(signUpRequest: SignUpRequest) {
        viewModelScope.launch {
            try {
                isSuccess.postValue(repository.createUser(signUpRequest))
            } catch (e: Exception) {
                isError.postValue(e.toString())
            }
        }
    }

}