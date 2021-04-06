package kz.pillikan.lombart.content.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kz.pillikan.lombart.content.model.FoundationRepository

class FoundationViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "FoundationViewModel"
    }

    private val repository = FoundationRepository(application)

    val isLanguage = MutableLiveData<Boolean>()

    val getIsFirstLanguage = MutableLiveData<Boolean>()
    val setIsFirstLanguage = MutableLiveData<Boolean>()
    val language = MutableLiveData<String>()

    fun setLanguage(language: String) {
        try {
            isLanguage.postValue(repository.setLanguage(language))
        } catch (e: Exception) {
            Log.e(TAG, "setLanguage: ${e.message}")
        }
    }

    fun getLanguage() {
        try {
            language.postValue(repository.language())
        } catch (e: NullPointerException) {
            Log.d(TAG, "getLanguage: ${e.message}")
        }
    }

    fun getIsFirstLanguage() {
        try {
            getIsFirstLanguage.postValue(repository.getIsFirstLanguage())
        } catch (e: Exception) {
            Log.e(TAG, "getIsFirstLanguage: ${e.message} ")
        }
    }

    fun setIsFirstLanguage() {
        try {
            setIsFirstLanguage.postValue(repository.setIsFirstLanguage())
        } catch (e: Exception) {
            Log.d(TAG, "setIsFirstLanguage: ${e.message}")
        }
    }
}