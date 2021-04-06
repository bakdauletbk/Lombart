package kz.pillikan.lombart.content.viewmodel.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.home.HomeRepository
import kz.pillikan.lombart.content.model.response.home.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "HomeViewModel"
    }

    var loanList: MutableLiveData<ArrayList<Tickets>> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val currencyList: MutableLiveData<ArrayList<CurrencyList>> = MutableLiveData()
    val weatherData: MutableLiveData<WeatherData> = MutableLiveData()
    val profileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()
    val slidersList: MutableLiveData<ArrayList<SlidersList>> = MutableLiveData()
    val finenessPrice: MutableLiveData<FinenessPriceResponse> = MutableLiveData()
    val headText: MutableLiveData<TitleResponse> = MutableLiveData()
    val getLanguage = MutableLiveData<String>()
    val setLanguage= MutableLiveData<Boolean>()

    private val repository: HomeRepository = HomeRepository(application)

    fun getLanguage() {
        try {
            getLanguage.postValue(repository.getLanguage())
        } catch (e: Exception) {
            Log.e(TAG, "getLanguage: ${e.message} ")
        }
    }

    fun setLanguage(language:String){
        try {
            setLanguage.postValue(repository.setLanguage(language))
        }catch (e:Exception){
            Log.e(TAG, "setLanguage: ${e.message} ")
        }
    }

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

    suspend fun getHeadText() {
        viewModelScope.launch {
            try {
                val response = repository.getHeadText()
                headText.postValue(response)
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}