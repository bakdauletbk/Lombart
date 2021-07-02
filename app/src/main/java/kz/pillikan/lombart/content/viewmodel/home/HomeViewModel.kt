package kz.pillikan.lombart.content.viewmodel.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.home.HomeRepository
import kz.pillikan.lombart.content.model.request.home.PayRequest
import kz.pillikan.lombart.content.model.response.home.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "HomeViewModel"
    }

    private val repository: HomeRepository = HomeRepository(application)

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    var loanList: MutableLiveData<ArrayList<Tickets>> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val currencyList: MutableLiveData<ArrayList<CurrencyList>> = MutableLiveData()
    val weatherData: MutableLiveData<WeatherData> = MutableLiveData()
    val profileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()
    val slidersList: MutableLiveData<ArrayList<SlidersList>> = MutableLiveData()
    val finenessPrice: MutableLiveData<FinenessPriceResponse> = MutableLiveData()
    val headText: MutableLiveData<TitleResponse> = MutableLiveData()
    val getLanguage = MutableLiveData<String>()
    val setLanguage = MutableLiveData<Boolean>()
    var cardList: MutableLiveData<CardListResponse> = MutableLiveData()
    val isPayLoans = MutableLiveData<Boolean>()

    fun clearSharedPref() {
        try {
            repository.clearSharedPref()
        } catch (e: Exception) {
        }
    }

    fun getLanguage() {
        try {
            getLanguage.postValue(repository.getLanguage())
        } catch (e: Exception) {
            Log.e(TAG, "getLanguage: ${e.message} ")
        }
    }

    fun setLanguage(language: String) {
        try {
            setLanguage.postValue(repository.setLanguage(language))
        } catch (e: Exception) {
            Log.e(TAG, "setLanguage: ${e.message} ")
        }
    }

    suspend fun getLoans() {
        viewModelScope.launch {
            try {
                val response = repository.getLoans()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> loanList.postValue(response.body()!!.tickets)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getCurrency() {
        viewModelScope.launch {
            try {
                val response = repository.getCurrency()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> currencyList.postValue(response.body()!!.currency)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getWeather() {
        viewModelScope.launch {
            try {
                val response = repository.getWeather()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> weatherData.postValue(response.body()!!.data)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getProfile()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> profileInfo.postValue(response.body()!!.profile)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getSliderList() {
        viewModelScope.launch {
            try {
                val response = repository.getSliderList()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> slidersList.postValue(response.body()!!.sliders)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getFinenessPrice() {
        viewModelScope.launch {
            try {
                val response = repository.getFinenessPrice()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> finenessPrice.postValue(response.body())
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getHeadText() {
        viewModelScope.launch {
            try {
                val response = repository.getHeadText()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> headText.postValue(response.body())
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getCards() {
        viewModelScope.launch {
            try {
                val response = repository.getCards()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> cardList.postValue(response.body()!!)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isError.postValue(null)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun payLoans(payRequest: PayRequest) {
        viewModelScope.launch {
            try {
                val response = repository.payLoans(payRequest)
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isPayLoans.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isPayLoans.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}