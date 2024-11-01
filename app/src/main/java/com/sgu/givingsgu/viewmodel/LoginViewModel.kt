package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.LoginResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.repository.AuthRepository
import com.sgu.givingsgu.utils.DataLocalManager
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel :ViewModel() {
    private val authRepository = AuthRepository()

    fun loginUser(email: String, password: String, callback: AuthCallback) {
        viewModelScope.launch {
            authRepository.loginUser(email, password).enqueue(object : Callback<ResponseWrapper<LoginResponse>> {
                override fun onResponse(call: Call<ResponseWrapper<LoginResponse>>, response: Response<ResponseWrapper<LoginResponse>>) {
                    if (response.isSuccessful) {
                        //luu lai token
                        DataLocalManager.saveToken(response.body()?.data?.token!!)

                        //luu lai user
                        DataLocalManager.saveUser(response.body()?.data?.user!!)

                        DataLocalManager.setLoggedIn(true)
                        callback.onSuccess()
                        // Xử lý thành công
                    } else {
                        // Xử lý thất bại
                        callback.onFailure("Login fail")
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<LoginResponse>>, t: Throwable) {
                    // Xử lý lỗi
                    callback.onFailure(t.message.toString())
                }
            })
        }
    }

    interface AuthCallback {
        fun onSuccess()
        fun onFailure(message: String)
    }

}