package kz.pillikan.lombart.content.viewmodel.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.pillikan.lombart.content.model.repository.profile.ChangePasswordRepository
import kz.pillikan.lombart.content.model.request.profile.ChangePassword
import kz.pillikan.lombart.content.model.request.profile.CheckPassword

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChangePasswordRepository = ChangePasswordRepository(application)

    val isError: MutableLiveData<String> = MutableLiveData()
    val isCheckPassword: MutableLiveData<Boolean> = MutableLiveData()
    val isChangePassword: MutableLiveData<Boolean> = MutableLiveData()

    suspend fun checkPassword(checkPassword: CheckPassword) {
        viewModelScope.launch {
            try {
                isCheckPassword.postValue(repository.checkPassword(checkPassword))
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

    suspend fun changePassword(changePassword: ChangePassword) {
        viewModelScope.launch {
            try {
                isChangePassword.postValue(repository.changePassword(changePassword))
            } catch (e: Exception) {
                isError.postValue(null)
            }
        }
    }

}