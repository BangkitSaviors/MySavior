package com.capstone.team.mysavior.data.pref

data class UserModel(
    val name: String? = null,
    val email: String,
    val token: String,
    val isLogin: Boolean = false,
    val birthDate: String? = null,
    val phoneNumber: String? = null,
    val profilePicture: String? = null
)