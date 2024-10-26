package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.AuthRequest
import com.sgu.givingsgu.model.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("/api/auth/login")
    fun login(@Body authRequest: AuthRequest): Call<AuthResponse>

    @POST("/api/auth/register")
    fun register(@Body authRequest: AuthRequest): Call<Void>
}