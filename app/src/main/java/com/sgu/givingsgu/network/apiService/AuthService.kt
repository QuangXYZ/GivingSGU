package com.sgu.givingsgu.network.apiService
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.request.RegisterRequest
import com.sgu.givingsgu.network.response.LoginResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("/api/auth/login")
    fun loginUser(
        @Query("username") username: String,
        @Query("password") password: String
    ): Call<ResponseWrapper<LoginResponse>>

    @POST("/api/auth/register")
    fun registerUser(@Body user: RegisterRequest): Call<ResponseWrapper<User>>}