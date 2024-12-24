package com.capstone.team.mysavior.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstone.team.mysavior.data.UserRepository
import com.capstone.team.mysavior.data.pref.UserModel
import kotlinx.coroutines.launch

class SettingViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun saveSession(user: UserModel, onSuccess: () -> Unit) {
        viewModelScope.launch {
            userRepository.saveSession(user)
            onSuccess()
        }
    }
}
