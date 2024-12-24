package com.capstone.team.mysavior.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.team.mysavior.data.UserRepository
import com.capstone.team.mysavior.data.pref.UserModel
import com.capstone.team.mysavior.data.remote.request.LoginRequest
import com.capstone.team.mysavior.data.remote.response.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _loginResult = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginResult: StateFlow<Result<LoginResponse>?> = _loginResult

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            try {
                val response = repository.login(request)
                if (response.error == false) {
                    response.loginResult?.let { loginResult ->
                        val user = loginResult.token?.let {
                            UserModel(
                                email = request.email,
                                token = it
                            )
                        }
                        user?.let { repository.saveSession(it) }
                    }
                }
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}