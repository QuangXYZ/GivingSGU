package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.repository.AuthRepository
import com.sgu.givingsgu.viewmodel.LoginViewModel.AuthCallback
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    private val authRepository = AuthRepository()

    fun registerUser(email : String, password : String, phone : String, fullName : String, callback: AuthCallback) {
        val user = User( email, password, fullName, phone)
        viewModelScope.launch {

            authRepository.registerUser(user).enqueue(object : Callback<ResponseWrapper<User>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<User>>,
                    response: Response<ResponseWrapper<User>>
                ) {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                        // Xử lý thành công
                    } else {
                        // Xử lý thất bại
                        callback.onFailure("Register fail")
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<User>>, t: Throwable) {
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