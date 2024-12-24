package com.capstone.team.mysavior.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.team.mysavior.data.UserRepository
import com.capstone.team.mysavior.data.pref.UserModel
import com.capstone.team.mysavior.data.remote.request.RegisterRequest
import com.capstone.team.mysavior.data.remote.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResult = MutableStateFlow<Result<RegisterResponse>?>(null)
    val registerResult: MutableStateFlow<Result<RegisterResponse>?> = _registerResult

    fun registerUser(request: RegisterRequest) {
        viewModelScope.launch {
            try {
                val response = repository.register(request)
                _registerResult.value = Result.success(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                _registerResult.value = Result.failure(Exception(errorResponse.message))
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}

