package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.ActivityDonationBinding
import com.sgu.givingsgu.databinding.ActivitySuccessBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.Transaction
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SuccessActivity : BaseActivity() {

    private lateinit var binding: ActivitySuccessBinding
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()
    }


    fun init() {
//        transaction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra("transaction", Transaction::class.java)!!
//        } else {
//            intent.getParcelableExtra("transaction")!!
//        }
        val amount = intent.getDoubleExtra("transaction", 0.0)
        val transactionId = intent.getStringExtra("transactionId")

        binding.textView9.text = formatCurrency(amount)
        binding.transactionDate.text = formatDate(Date())
        binding.transactionId.text = transactionId
    }

    private fun settingUpListener() {
        binding.home.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun formatCurrency(amount: Double): String {
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(amount)
        return "$formatted VND"
    }

    fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
    }

}