package com.sgu.givingsgu.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.card.MaterialCardView
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.ActivityDonationBinding
import com.sgu.givingsgu.databinding.ActivityMainBinding
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.viewmodel.DonationViewModel
import vn.momo.momo_partner.AppMoMoLib
import vn.momo.momo_partner.MoMoParameterNamePayment
import vn.momo.momo_partner.utils.MoMoConfig
import java.text.NumberFormat
import java.util.Locale

class DonationActivity : BaseActivity() {
    private lateinit var binding: ActivityDonationBinding
    private var lastClickedCard: MaterialCardView? = null
    private var lastClickedTextView: TextView? = null
    private var current = ""
    private lateinit var viewModel : DonationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()
    }


    private fun init() {
        viewModel = DonationViewModel()
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.PRODUCTION) // Hoặc AppMoMoLib.ENVIRONMENT.PRODUCTION


    }
    private fun settingUpListener() {
        setCardListener(binding.cardView1, binding.textView1)
        setCardListener(binding.cardView2, binding.textView2)
        setCardListener(binding.cardView3, binding.textView3)
        setCardListener(binding.cardView4, binding.textView4)
        setCardListener(binding.cardView5, binding.textView5)
        setCardListener(binding.cardView6, binding.textView6)

        binding.donateBtn.setOnClickListener(View.OnClickListener {
            requestPayment()
        })


    }
    private fun setCardListener(cardView: MaterialCardView, textView: TextView) {
            cardView.setOnClickListener {
                lastClickedCard?.setCardBackgroundColor(
                    ContextCompat.getColor(this, R.color.white)
                )
                lastClickedTextView?.setTextColor(
                    ContextCompat.getColor(this, R.color.text_color)
                )
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(this, R.color.orange)
                )
                textView.setTextColor(
                    ContextCompat.getColor(this, R.color.white)
                )
                lastClickedCard = cardView
                lastClickedTextView = textView
                binding.amount.setText(textView.text.toString())
            }


            binding.amount.addTextChangedListener(object : TextWatcher {

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString() != current) {
                        binding.amount.removeTextChangedListener(this)

                        // Loại bỏ định dạng cũ
                        val cleanString = s.toString().replace("""[,.]""".toRegex(), "")

                        // Chuyển chuỗi về dạng số
                        val parsed = cleanString.toDoubleOrNull() ?: 0.0

                        // Định dạng số theo kiểu tiền tệ
                        val formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed)

                        current = formatted
                        binding.amount.setText(formatted)
                        binding.amount.setSelection(formatted.length)

                        binding.amount.addTextChangedListener(this)
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

        binding.previous.setOnClickListener {
            finish()
        }
        binding.momoPayment.setOnClickListener{
            binding.momoPayment.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.zaloPayment.strokeColor = ContextCompat.getColor(this, R.color.lightGrey)

        }

        binding.zaloPayment.setOnClickListener{
            binding.zaloPayment.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.momoPayment.strokeColor = ContextCompat.getColor(this, R.color.lightGrey)

        }

    }


    private fun requestPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        // Prepare data for payment request
        val requestData = HashMap<String, Any?>()

        requestData[MoMoParameterNamePayment.MERCHANT_NAME] = "MOMO_DEMO"
        requestData[MoMoParameterNamePayment.MERCHANT_CODE] = "MOMOT5BZ20231213_TEST"
        requestData[MoMoParameterNamePayment.MERCHANT_NAME_LABEL] = "Merchant Name Label"
        requestData[MoMoParameterNamePayment.AMOUNT] = binding.amount.text.toString().replace(",", "")
        requestData[MoMoParameterNamePayment.DESCRIPTION] = "Payment for X"

        // Payment code (unique for each transaction)
        requestData[MoMoParameterNamePayment.REQUEST_ID] = System.currentTimeMillis().toString()
        requestData[MoMoParameterNamePayment.PARTNER_CODE] = "MOMOT5BZ20231213_TEST"
        requestData[MoMoParameterNamePayment.LANGUAGE] = "vi" // or "en"
        requestData[MoMoParameterNamePayment.REQUEST_TYPE] = "payment"

        // Extra data (optional)
        requestData[MoMoParameterNamePayment.EXTRA_DATA] = "" // Optional data

        AppMoMoLib.getInstance().requestMoMoCallBack(this, requestData)
    }

    // Xử lý kết quả trả về
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == RESULT_OK) {
            if (data != null) {
                val status = data.getIntExtra("status", -1)

                if (status == 0) { // Thành công
                    val message = data.getStringExtra("message")
                    val token = data.getStringExtra("data").toString() // Dữ liệu token trả về từ MoMo
                    val amount = data.getStringExtra("amount").toString()

//                    val transactionRequest = TransactionRequest(userId, , amount.toDouble(), "MoMo", token)

                    viewModel.saveTransaction(1, 1, amount.toDouble(), "MoMo")
                    Toast.makeText(this, "Payment success! Token: $token", Toast.LENGTH_LONG).show()
                } else {
                    // Thất bại
                    val message = data.getStringExtra("message")
                    Toast.makeText(this, "Payment failed: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



}