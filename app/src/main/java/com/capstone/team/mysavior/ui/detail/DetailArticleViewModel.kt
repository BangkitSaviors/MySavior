package com.capstone.team.mysavior.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.capstone.team.mysavior.data.pref.UserModel

class DetailArticleViewModel : ViewModel() {
    private val _selectedArticle = MutableLiveData<UserModel>()
    val selectedArticle: LiveData<UserModel> = _selectedArticle

    fun setArticle(article: UserModel) {
        _selectedArticle.value = article
    }
}