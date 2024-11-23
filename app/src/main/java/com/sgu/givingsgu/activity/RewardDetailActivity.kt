package com.sgu.givingsgu.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.databinding.ActivityRewardDetailBinding
import com.sgu.givingsgu.model.UserReward


class RewardDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityRewardDetailBinding
    private lateinit var userReward: UserReward





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRewardDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()

    }

    private fun init() {
        userReward = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("reward", UserReward::class.java)!!
        } else {
            intent.getParcelableExtra("reward")!!
        }


        binding.rewardName.text = userReward.reward.name
        binding.rewardId.text = userReward.id.toString()
        binding.rewardPoints.text = userReward.reward.pointsRequired.toString()
        binding.rewardStatus.text = userReward.status




        val qrContent: String = userReward.id.toString() //Content to encode in QR Code

        //initializing MultiFormatWriter for QR code
        val mWriter = MultiFormatWriter()
        try {
            //BitMatrix class to encode entered text and set Width & Height
            val mMatrix = mWriter.encode(qrContent, BarcodeFormat.QR_CODE, 800, 800)
            val mEncoder = BarcodeEncoder()
            val mBitmap = mEncoder.createBitmap(mMatrix) //creating bitmap of code
            binding.qrcode.setImageBitmap(mBitmap) //Setting generated QR code to imageView
        } catch (e: WriterException) {
            e.printStackTrace()
        }



    }

}