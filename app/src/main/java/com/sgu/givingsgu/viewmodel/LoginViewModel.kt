package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.request.TransactionRequest
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






    fun registerUser(username : String, email : String, password : String, phone : String, facultyId : Long, fullName : String, studentId : String, callback: AuthCallback) {
        val user = User(username, password, email, phone, facultyId, fullName, studentId)

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
    fun loginUser(email: String, password: String, callback: AuthCallback) {
        viewModelScope.launch {
            authRepository.loginUser(email, password).enqueue(object : Callback<ResponseWrapper<String>> {
                override fun onResponse(call: Call<ResponseWrapper<String>>, response: Response<ResponseWrapper<String>>) {
                    if (response.isSuccessful) {
                        //luu lai token
                        DataLocalManager.saveToken(response.body()?.data!!)
                        DataLocalManager.setLoggedIn(true)
                        callback.onSuccess()
                        // Xử lý thành công
                    } else {
                        // Xử lý thất bại
                        callback.onFailure("Login fail")
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<String>>, t: Throwable) {
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