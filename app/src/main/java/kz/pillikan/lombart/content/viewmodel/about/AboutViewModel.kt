package kz.pillikan.lombart.content.viewmodel.about

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.about.AboutRepository
import kz.pillikan.lombart.content.model.response.about.AddressList
import kotlin.Exception

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AboutRepository = AboutRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val addressList: MutableLiveData<ArrayList<AddressList>> = MutableLiveData()
    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun getAddress() {
        viewModelScope.launch {
            try {
                val response = repository.getAddress()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> addressList.postValue(response.body()!!.data)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
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