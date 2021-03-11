package kz.pillikan.lombart.splash

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "SplashViewModel"
    }

    private var repository: SplashRepository = SplashRepository(application)

    var isNetworkConnection = MutableLiveData<Boolean>()
    var isAuthorize = MutableLiveData<Boolean>()
    var isError = MutableLiveData<Boolean>()

    fun checkNetwork(context: Context) {
        viewModelScope.launch {
            try {
                isNetworkConnection.postValue(repository.checkNetwork(context))
            } catch (e: Exception) {
                isNetworkConnection.postValue(false)
            }
        }
    }

    fun checkAuthorize() {
        viewModelScope.launch {
            try {
                isAuthorize.postValue(repository.checkAuthorize())
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                isError.postValue(true)
            }
        }
    }

}