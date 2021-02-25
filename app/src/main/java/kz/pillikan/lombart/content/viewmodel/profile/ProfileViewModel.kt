package kz.pillikan.lombart.content.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.profile.ProfileRepository
import kz.pillikan.lombart.content.model.response.home.ProfileInfo
import kz.pillikan.lombart.content.model.response.profile.CardModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    var cardList: MutableLiveData<List<CardModel>> = MutableLiveData()
    var profileInfo: MutableLiveData<ProfileInfo> = MutableLiveData()
    val isError: MutableLiveData<String> = MutableLiveData()

    private val repository: ProfileRepository = ProfileRepository(application)

    fun getCard() {
        viewModelScope.launch {
            try {
                val response = repository.getCard()
                cardList.postValue(response as List<CardModel>?)
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