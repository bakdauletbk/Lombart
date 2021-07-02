package kz.pillikan.lombart.content.viewmodel.profile

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.common.remote.Constants
import kz.pillikan.lombart.content.model.repository.profile.ProfileRepository
import kz.pillikan.lombart.content.model.request.profile.DeleteCardRequest
import kz.pillikan.lombart.content.model.response.home.CardList
import kz.pillikan.lombart.content.model.response.home.ProfileInfo

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    var cardList: MutableLiveData<List<CardList>> = MutableLiveData()
    var profileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()
    val isLogout: MutableLiveData<Boolean> = MutableLiveData()
    val isDeletedCard: MutableLiveData<Boolean> = MutableLiveData()

    val isUpdateApp = MutableLiveData<Boolean>()
    val isUnAuthorized = MutableLiveData<Boolean>()

    private val repository: ProfileRepository = ProfileRepository(application)

    fun deleteCard(deleteCardRequest: DeleteCardRequest) {
        viewModelScope.launch {
            try {
                val response = repository.deleteCard(deleteCardRequest)
                Log.d("response test", response.isSuccessful.toString())
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> isDeletedCard.postValue(true)
                    Constants.RESPONSE_UPDATE_APP -> isUpdateApp.postValue(true)
                    Constants.RESPONSE_UNAUTHORIZED -> isUnAuthorized.postValue(true)
                    else -> isDeletedCard.postValue(false)
                }
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun getCard() {
        viewModelScope.launch {
            try {
                val response = repository.getCard()
                when (response.code()) {
                    Constants.RESPONSE_SUCCESS_CODE -> cardList.postValue(response.body()!!.cards)
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

    fun clearSharedPref() {
        try {
            repository.clearSharedPref()
        } catch (e: Exception) {
        }
    }

    fun logout() {
        isLogout.postValue(repository.logout())
    }

}