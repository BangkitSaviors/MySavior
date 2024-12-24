package com.capstone.team.mysavior.di

import android.content.Context
import com.capstone.team.mysavior.data.UserRepository
import com.capstone.team.mysavior.data.pref.UserPreference
import com.capstone.team.mysavior.data.pref.dataStore
import com.capstone.team.mysavior.data.remote.retrofit.ApiConfig
import com.capstone.team.mysavior.ui.setting.SettingViewModel

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(pref, apiService)
    }
    fun provideSettingViewModel(context: Context): SettingViewModel {
        val userRepository = provideRepository(context)
        return SettingViewModel(userRepository)
    }
}