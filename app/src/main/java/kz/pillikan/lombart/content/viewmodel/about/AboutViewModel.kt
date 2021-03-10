package kz.pillikan.lombart.content.viewmodel.about

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.about.AboutRepository
import kz.pillikan.lombart.content.model.response.about.AboutResponse
import kz.pillikan.lombart.content.model.response.about.AddressList
import kotlin.Exception

class AboutViewModel(application: Application) : AndroidViewModel(application) {

    private var repository: AboutRepository = AboutRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val addressList: MutableLiveData<ArrayList<AddressList>> = MutableLiveData()
    val about: MutableLiveData<AboutResponse> = MutableLiveData()

    suspend fun getAddress() {
        viewModelScope.launch {
            try {
                val response = repository.getAddress()
                if (response != null) {
                    addressList.postValue(response)
                } else {
                    addressList.postValue(null)
                }
                addressList.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getAbout() {
        viewModelScope.launch {
            try {
                val response = repository.getAbout()
                about.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}