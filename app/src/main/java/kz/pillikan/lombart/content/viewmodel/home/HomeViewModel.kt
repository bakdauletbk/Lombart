package kz.pillikan.lombart.content.viewmodel.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.home.HomeRepository
import kz.pillikan.lombart.content.model.response.home.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    var loanList: MutableLiveData<ArrayList<Tickets>> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val serviceCurrency: MutableLiveData<ArrayList<Currency>> = MutableLiveData()
    val weatherData: MutableLiveData<WeatherData> = MutableLiveData()
    val profileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()

    private val repository: HomeRepository = HomeRepository(application)

    fun getLoans() {
        viewModelScope.launch {
            try {
                val response = repository.getLoans()
                if (response != null) {
                    loanList.postValue(response)
                } else {
                    loanList.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    fun getCurrency() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrency()
                if (response != null) {
                    serviceCurrency.postValue(response)
                } else {
                    serviceCurrency.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    fun getWeather() {
        viewModelScope.launch {
            try {
                val response = repository.getWeather()
                if (response != null) {
                    weatherData.postValue(response)
                } else {
                    weatherData.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                if (response != null) {
                    profileInfo.postValue(response)
                } else {
                    profileInfo.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}