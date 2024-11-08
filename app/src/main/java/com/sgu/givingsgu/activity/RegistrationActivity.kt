package com.sgu.givingsgu.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.activity.Fragment.HomeFragment
import com.sgu.givingsgu.activity.Fragment.RegisterStepOneFragment
import com.sgu.givingsgu.databinding.ActivityRegistrationBinding
import com.sgu.givingsgu.viewmodel.LoginViewModel
import com.sgu.givingsgu.viewmodel.RegistrationViewModel


class RegistrationActivity : BaseActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var viewModel : RegistrationViewModel
    private lateinit var stepOneFragment: RegisterStepOneFragment
    private var activeFragment: Fragment? = null

    private val registerActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
        stepOneFragment = RegisterStepOneFragment()
        activeFragment = stepOneFragment


        supportFragmentManager.beginTransaction().apply {
            add(binding.contentFrame.id, stepOneFragment, "stepOne").show(stepOneFragment)
        }.commit()

        binding.imageView3.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                    binding.progressIndicator.progress = 50 // Reset progress to 50% when navigating back to step one
                    binding.textView.setText("STEP 1 OF 2")
                } else {
                    finish()
                }
            }
        })


//        viewModel = RegistrationViewModel()
//        binding.backButton.setOnClickListener {
//            finish() // Close RegistrationActivity and return to LoginActivity
//        }
//
//        binding.registerButton.setOnClickListener(View.OnClickListener {
//
//            val email = binding.usernameEditText.text.toString().trim()
//            val password = binding.passwordEditText.text.toString().trim()
//            val fullname = binding.emailEditText.text.toString().trim()
//            val phone = binding.phoneEditText.text.toString().trim()
//
//            if (email.isNotEmpty() && password.isNotEmpty() && fullname.isNotEmpty() && phone.isNotEmpty()) {
//                viewModel.registerUser(email, password, fullname, phone, object : RegistrationViewModel.AuthCallback {
//                    override fun onSuccess() {
//                        Toast.makeText(this@RegistrationActivity, "Login success", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
//                        startActivity(intent)
//                    }
//
//                    override fun onFailure(message: String) {
//                        Toast.makeText(this@RegistrationActivity, message, Toast.LENGTH_SHORT).show()
//                    }
//                })
//            } else {
//                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
//            }
//        })
    }


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}