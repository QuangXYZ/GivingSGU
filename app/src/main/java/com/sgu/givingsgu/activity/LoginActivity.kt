package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.activity.Fragment.ProfileFragment
import com.sgu.givingsgu.databinding.ActivityLoginBinding

import com.sgu.givingsgu.viewmodel.LoginViewModel


class LoginActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel : LoginViewModel

    private val registerActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }


    fun init() {

        // init
        viewModel = LoginViewModel()
        binding.forgotPasswordTextView.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            registerActivityResultLauncher.launch(intent)
        }

        binding.loginButton.setOnClickListener(View.OnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.loginUser(username, password, object : LoginViewModel.AuthCallback {
                    override fun onSuccess() {
                        Toast.makeText(this@LoginActivity, "Login success", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(message: String) {
                        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        })
    }

}