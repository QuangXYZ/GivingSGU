package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.ActivityDonationBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.utils.DataLocalManager
import com.sgu.givingsgu.viewmodel.DonationViewModel
import com.sgu.givingsgu.zalo.Helper.CreateOrder
import vn.momo.momo_partner.AppMoMoLib
import vn.momo.momo_partner.MoMoParameterNamePayment
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener
import java.text.NumberFormat
import java.util.Locale

class DonationActivity : BaseActivity() {
    private lateinit var binding: ActivityDonationBinding
    private var lastClickedCard: MaterialCardView? = null
    private var lastClickedTextView: TextView? = null
    private var current = ""
    private lateinit var viewModel: DonationViewModel
    private lateinit var project: Project
    private var paymentMethod = 0;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()
    }


    private fun init() {
        viewModel = DonationViewModel()

        // MoMo SDK Init
        AppMoMoLib.getInstance()
            .setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT) // Hoặc AppMoMoLib.ENVIRONMENT.PRODUCTION

        // ZaloPay SDK Init
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        ZaloPaySDK.init(2553, Environment.SANDBOX)


        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("project", Project::class.java)!!
        } else {
            intent.getParcelableExtra("project")!!
        }

        if (project.imageUrls != null) {
            val images = project.imageUrls?.split(",")?.toList()
            images?.let {
                Glide.with(this).load(it[0]).into(binding.image)
            }
        }
        binding.name.text = project.name

        if (DataLocalManager.getUser() != null) {
            binding.anonymous.isEnabled = true
            binding.anonymous.isChecked = false
        } else {
            binding.anonymous.isEnabled = false
            binding.anonymous.isChecked = true
        }

    }

    private fun settingUpListener() {
        setCardListener(binding.cardView1, binding.textView1)
        setCardListener(binding.cardView2, binding.textView2)
        setCardListener(binding.cardView3, binding.textView3)
        setCardListener(binding.cardView4, binding.textView4)
        setCardListener(binding.cardView5, binding.textView5)
        setCardListener(binding.cardView6, binding.textView6)

        binding.momoPayment.setOnClickListener {
            paymentMethod = 0
            binding.momoPayment.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.zaloPayment.strokeColor = ContextCompat.getColor(this, R.color.lightGrey)
        }
        binding.zaloPayment.setOnClickListener {
            paymentMethod = 1
            binding.zaloPayment.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.momoPayment.strokeColor = ContextCompat.getColor(this, R.color.lightGrey)
        }

        binding.donateBtn.setOnClickListener(View.OnClickListener {
            if (paymentMethod == 0) {
                requestMomoPayment()
            } else {
                requestZaloPayPayment()
            }
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
        binding.momoPayment.setOnClickListener {
            binding.momoPayment.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.zaloPayment.strokeColor = ContextCompat.getColor(this, R.color.lightGrey)

        }

        binding.zaloPayment.setOnClickListener {
            binding.zaloPayment.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.momoPayment.strokeColor = ContextCompat.getColor(this, R.color.lightGrey)

        }

    }


    private fun requestMomoPayment() {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);
        // Prepare data for payment request
        val requestData = HashMap<String, Any?>()

        requestData[MoMoParameterNamePayment.MERCHANT_NAME] = "MOMO_DEMO"
        requestData[MoMoParameterNamePayment.MERCHANT_CODE] = "MOMOIQA420180417977366"
        requestData[MoMoParameterNamePayment.MERCHANT_NAME_LABEL] = "Merchant Name Label"
        requestData[MoMoParameterNamePayment.AMOUNT] =
            binding.amount.text.toString().replace(",", "")
        requestData[MoMoParameterNamePayment.DESCRIPTION] = "Payment for donation"

        // Payment code (unique for each transaction)
        requestData[MoMoParameterNamePayment.REQUEST_ID] = System.currentTimeMillis().toString()
        requestData[MoMoParameterNamePayment.PARTNER_CODE] = "MOMOIQA420180417"
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
                    val token =
                        data.getStringExtra("data").toString() // Dữ liệu token trả về từ MoMo
                    val amount = data.getStringExtra("amount").toString()

//                    val transactionRequest = TransactionRequest(userId, , amount.toDouble(), "MoMo", token)

//                    DataLocalManager.getUser()
//                        ?.let {
//                            viewModel.saveTransaction(
//                                it.userId,
//                                project.projectId,
//                                amount.toDouble(),
//                                "MoMo",
//
//                            )
//                        }
                    Toast.makeText(this, "Payment success! Token: $token", Toast.LENGTH_LONG).show()
                } else {
                    // Thất bại
                    val message = data.getStringExtra("message")
                    Toast.makeText(this, "Payment failed: $message", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    private fun requestZaloPayPayment() {
        val orderApi = CreateOrder()
        try {
            val data = orderApi.createOrder(binding.amount.text.toString().replace(",", ""))
            val code = data.getString("return_code")
            if (code == "1") {
                var token = data.getString("zp_trans_token")
                ZaloPaySDK.getInstance().payOrder(
                    this,
                    token,
                    "demozpdk://app",
                    object : PayOrderListener {
                        override fun onPaymentSucceeded(
                            transactionId: String,
                            transToken: String,
                            appTransID: String

                        ) {

                            val userId = if (!binding.anonymous.isChecked) {
                                DataLocalManager.getUser()?.userId
                            } else {
                                30 // default user
                            }



                                    viewModel.saveTransaction(
                                        transactionId,
                                        userId!!,
                                        project.projectId,
                                        binding.amount.text.toString().replace(",", "").toDouble(),
                                        "Zalo Pay",
                                        transToken,
                                        object : DonationViewModel.TransactionCallback {
                                            override fun onSuccess(transaction: Transaction?) {
                                                val intent = Intent(this@DonationActivity, SuccessActivity::class.java)
                                                intent.putExtra("transaction" ,binding.amount.text.toString().replace(",", "").toDouble() )
                                                intent.putExtra("transactionId", transactionId)
                                                startActivity(intent)
                                                finish()
                                            }
                                            override fun onFail(error: String) {
                                            }
                                        }
                                    )
                                }




                        override fun onPaymentCanceled(zpTransToken: String, appTransID: String) {
                            MaterialAlertDialogBuilder(this@DonationActivity)
                                .setTitle("User Cancel Payment")
                                .setBackground(ContextCompat.getDrawable(this@DonationActivity, R.color.white))
                                .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                                .setPositiveButton(
                                    "OK"
                                ) { dialog, which -> }
                                .setNegativeButton("Cancel", null).show()
                        }

                        override fun onPaymentError(
                            zaloPayError: ZaloPayError,
                            zpTransToken: String,
                            appTransID: String
                        ) {
                            MaterialAlertDialogBuilder(this@DonationActivity)
                                .setTitle("Payment Fail")
                                .setBackground(ContextCompat.getDrawable(this@DonationActivity, R.color.white))
                                .setMessage(
                                    String.format(
                                        "ZaloPayErrorCode: %s \nTransToken: %s",
                                        zaloPayError.toString(),
                                        zpTransToken
                                    )
                                )
                                .setPositiveButton(
                                    "OK"
                                ) { dialog, which -> }
                                .setNegativeButton("Cancel", null).show()
                        }
                    })
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }

}