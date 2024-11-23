package com.sgu.givingsgu.activity

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.GridLayoutManager
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.adapter.RewardAdapter
import com.sgu.givingsgu.databinding.ActivityRedeemBinding
import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.UserReward
import com.sgu.givingsgu.utils.DataLocalManager
import com.sgu.givingsgu.viewmodel.RewardViewModel
import com.thecode.aestheticdialogs.AestheticDialog
import com.thecode.aestheticdialogs.DialogAnimation
import com.thecode.aestheticdialogs.DialogStyle
import com.thecode.aestheticdialogs.DialogType
import com.thecode.aestheticdialogs.OnDialogClickListener


class RewardActivity : BaseActivity(), RewardAdapter.OnRewardClickListener {
    private lateinit var binding: ActivityRedeemBinding
    private lateinit var viewModel: RewardViewModel
    private lateinit var reward: Reward

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRedeemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        enableEdgeToEdge()
        viewModel = RewardViewModel()
        viewModel.fetchAllReward()
        viewModel.reward.observe(this) {
            binding.recyclerView.adapter = RewardAdapter(it.toMutableList(), this)
            binding.recyclerView.isNestedScrollingEnabled = true
            binding.recyclerView.layoutManager =
                GridLayoutManager(this, 2)

        }
        viewModel.error.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.submitBtn.setOnClickListener {

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
                        }
                    })
                    .show()
                return@setOnClickListener
            }

            viewModel.redeem(reward.id.toString(), DataLocalManager.getUser()?.userId.toString(), object :
                RewardViewModel.RewardListener {
                override fun onRewardSuccess(userReward: UserReward) {

                    AestheticDialog.Builder(this@RewardActivity, DialogStyle.FLAT, DialogType.SUCCESS)
                        .setTitle("Success")
                        .setMessage("Redeem success")
                        .setCancelable(false)
                        .setDarkMode(false)
                        .setGravity(Gravity.CENTER)
                        .setAnimation(DialogAnimation.SHRINK)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()


                    finish()
                }

                override fun onRewardFailed(message: String) {
                    AestheticDialog.Builder(this@RewardActivity, DialogStyle.FLAT, DialogType.ERROR)
                        .setTitle("Error")
                        .setMessage("Redeem failed")
                        .setCancelable(false)
                        .setDarkMode(false)
                        .setGravity(Gravity.CENTER)
                        .setAnimation(DialogAnimation.SHRINK)
                        .setOnClickListener(object : OnDialogClickListener {
                            override fun onClick(dialog: AestheticDialog.Builder) {
                                dialog.dismiss()
                            }
                        })
                        .show()

                }


            })

        }
    }

    override fun onRewardClick(reward: Reward) {
        this.reward = reward
        binding.submitBtn.isEnabled = true
    }

    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {
        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.setDuration(1500)
        valueAnimator.addUpdateListener { valueAnimator ->
            textview.text = valueAnimator.animatedValue.toString()
        }
        valueAnimator.start()
    }
}