package com.capstone.team.mysavior.data

import com.capstone.team.mysavior.data.pref.UserModel
import com.capstone.team.mysavior.data.pref.UserPreference
import com.capstone.team.mysavior.data.remote.request.LoginRequest
import com.capstone.team.mysavior.data.remote.request.RegisterRequest
import com.capstone.team.mysavior.data.remote.response.LoginResponse
import com.capstone.team.mysavior.data.remote.response.RegisterResponse
import com.capstone.team.mysavior.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {

    suspend fun register(request: RegisterRequest): RegisterResponse {
        return apiService.register(request)
    }

    suspend fun login(request: LoginRequest): LoginResponse {
        return apiService.login(request)
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService)
            }.also { instance = it }
    }
}