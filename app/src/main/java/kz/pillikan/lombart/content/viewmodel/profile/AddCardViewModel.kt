package kz.pillikan.lombart.content.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.profile.AddCardRepository
import kz.pillikan.lombart.content.model.request.profile.CardRequest
import kz.pillikan.lombart.content.model.response.profile.CardResponse
import java.lang.Exception

class AddCardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AddCardRepository(application)

    val isError = MutableLiveData<String>()
    val cardResponse = MutableLiveData<CardResponse>()
    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    suspend fun addCard(cardRequest: CardRequest) {
        viewModelScope.launch {
            try {
                val response = repository.addCard(cardRequest)

                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> cardResponse.postValue(response.body())
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
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