package com.sgu.givingsgu.activity

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgu.givingsgu.adapter.RewardHistoryAdapter
import com.sgu.givingsgu.databinding.ActivityRewardHistoryBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.UserReward
import com.sgu.givingsgu.utils.DataLocalManager
import com.sgu.givingsgu.viewmodel.RewardHistoryViewModel
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogAnimation
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RewardHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRewardHistoryBinding
    private lateinit var viewModel : RewardHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRewardHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()

    }

    private fun init() {
        viewModel = RewardHistoryViewModel()
        if (DataLocalManager.getUser() == null) {
            AestheticDialog.Builder(this, DialogStyle.FLAT, DialogType.SUCCESS)
                .setTitle("Notice")
                .setMessage("Please login to redeem reward")
                .setCancelable(false)
                .setDarkMode(false)
                .setGravity(Gravity.CENTER)
                .setAnimation(DialogAnimation.SHRINK)
                .setOnClickListener(object : OnDialogClickListener {
                    override fun onClick(dialog: AestheticDialog.Builder) {
                        dialog.dismiss()
                        finish()

                    }
                })
                .show()
        }
        else {
            viewModel.fetchAllUserReward(DataLocalManager.getUser()!!.userId)

        }



        viewModel.reward.observe(this) {
            binding.recyclerView.adapter = RewardHistoryAdapter(it.toMutableList())
            binding.recyclerView.isNestedScrollingEnabled = true
            binding.recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        }

    }

    private fun settingUpListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }


}