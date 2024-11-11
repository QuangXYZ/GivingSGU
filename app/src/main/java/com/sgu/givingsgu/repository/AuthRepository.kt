package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.AuthService
import com.sgu.givingsgu.network.apiService.CommentService
import com.sgu.givingsgu.network.request.RegisterRequest
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.network.response.LoginResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import retrofit2.Call

class AuthRepository {
    private val authApiService = RetrofitClient.createService(AuthService::class.java)
    fun loginUser(email: String, password: String): Call<ResponseWrapper<LoginResponse>> {
        return authApiService.loginUser(email, password)
    }
    fun registerUser(user : RegisterRequest): Call<ResponseWrapper<User>> {
        return authApiService.registerUser(user)
    }
}