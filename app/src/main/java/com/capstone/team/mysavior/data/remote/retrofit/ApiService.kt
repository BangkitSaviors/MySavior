package com.capstone.team.mysavior.data.remote.retrofit

import com.capstone.team.mysavior.data.remote.request.LoginRequest
import com.capstone.team.mysavior.data.remote.request.RegisterRequest
import com.capstone.team.mysavior.data.remote.response.ArticleResponse
import com.capstone.team.mysavior.data.remote.response.LoginResponse
import com.capstone.team.mysavior.data.remote.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @POST("register")
    @Headers("Content-Type: application/json")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("login")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @GET("articles")
    @Headers("Content-Type: application/json")
    suspend fun getArticle(): ArticleResponse
}
