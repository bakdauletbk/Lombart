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
    val currencyList: MutableLiveData<ArrayList<CurrencyList>> = MutableLiveData()
    val weatherData: MutableLiveData<WeatherData> = MutableLiveData()
    val profileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()
    val slidersList: MutableLiveData<ArrayList<SlidersList>> = MutableLiveData()
    val finenessPrice: MutableLiveData<FinenessPriceResponse> = MutableLiveData()

    private val repository: HomeRepository = HomeRepository(application)

    suspend fun getLoans() {
        viewModelScope.launch {
            try {
                val response = repository.getLoans()
                loanList.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getCurrency() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrency()
                currencyList.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getWeather() {
        viewModelScope.launch {
            try {
                val response = repository.getWeather()
                weatherData.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                profileInfo.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getSliderList() {
        viewModelScope.launch {
            try {
                val response = repository.getSliderList()
                slidersList.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getFinenessPrice() {
        viewModelScope.launch {
            try {
                val response = repository.getFinenessPrice()
                finenessPrice.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}