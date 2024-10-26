package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.databinding.ActivityRegistrationBinding
import com.sgu.givingsgu.model.AuthRequest
import com.sgu.givingsgu.model.AuthResponse
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    fun init(){

        binding.registerButton.setOnClickListener {

            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                registerUser(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser(username: String, password: String) {
        val retrofit = RetrofitClient.getClient("https://givingsguserver-production.up.railway.app/") // Thay thế <server-ip> bằng địa chỉ IP của máy chủ
        val authService = retrofit.create(AuthService::class.java)

        val registerRequest = AuthRequest(username, password)
        authService.register(registerRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    binding.registerButton.text = "Registration successful"
                } else {
                    binding.registerButton.text = "Registration failed: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                binding.registerButton.text = "Error: ${t.message}"
            }
        })
    }
}