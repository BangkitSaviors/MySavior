package com.capstone.team.mysavior.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.team.mysavior.data.remote.response.ArticlesItem
import com.capstone.team.mysavior.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class HomeViewModel : ViewModel() {
    companion object {
        private val TAG = HomeViewModel::class.java.simpleName
    }
    private val _listArticle = MutableLiveData<List<ArticlesItem>>()
    val listArticles: LiveData<List<ArticlesItem>> get() = _listArticle
    val isLoading = MutableLiveData<Boolean>()

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    fun getListArticle() {
        isLoading.value = true
        val client = ApiConfig.getApiService()

        viewModelScope.launch {
            try {
                val response = client.getArticle()
                _listArticle.value = response.articles?.filterNotNull() ?: listOf()
                isLoading.value = false
            } catch (e: HttpException) {
                isLoading.value = false
                Log.e(TAG, "HTTP error: ${e.message}")
            } catch (e: IOException) {
                isLoading.value = false
                Log.e(TAG, "Network error: ${e.message}")
            } catch (e: Exception) {
                isLoading.value = false
                Log.e(TAG, "Unknown error: ${e.message}")
            }
        }
    }

}