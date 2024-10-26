package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.databinding.ActivityLoginBinding
import com.sgu.givingsgu.model.AuthRequest
import com.sgu.givingsgu.model.AuthResponse
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    fun init() {
        // init
        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginUser(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loginUser(username: String, password: String) {

    }

}