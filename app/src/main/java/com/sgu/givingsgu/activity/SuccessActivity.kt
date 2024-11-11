package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.ActivityDonationBinding
import com.sgu.givingsgu.databinding.ActivitySuccessBinding

class SuccessActivity : BaseActivity() {

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        settingUpListener()
    }


    private fun settingUpListener() {
        binding.home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

}