package com.sgu.givingsgu.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.R
import com.sgu.givingsgu.activity.ProjectDetailActivity
import com.sgu.givingsgu.activity.RewardDetailActivity
import com.sgu.givingsgu.activity.RewardHistoryActivity
import com.sgu.givingsgu.databinding.SingleRewardHistoryItemBinding
import com.sgu.givingsgu.databinding.SingleRewardItemBinding
import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.UserReward
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RewardHistoryAdapter (val rewards: MutableList<UserReward>) :
    RecyclerView.Adapter<RewardHistoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleRewardHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (rewards[position].reward?.imageUrl?.isNotEmpty() == true) {
            Glide.with(context).load(rewards[position].reward?.imageUrl).into(holder.binding.imageView10)
        }
        holder.binding.tvRewardName.text = rewards[position].reward?.name
        holder.binding.tvStatus.text = rewards[position].status
        holder.binding.tvDate.text = formatDateWithCustomPattern( rewards[position].redeemDate, "dd/MM/yyyy")

        holder.binding.root.setOnClickListener{
            val intent = Intent(holder.itemView.context, RewardDetailActivity::class.java)
            intent.putExtra("reward", rewards[position])
            holder.itemView.context.startActivity(intent)

        }



    }

    override fun getItemCount(): Int = rewards.size

    class ViewHolder(val binding: SingleRewardHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun formatDateWithCustomPattern(date: Date, pattern: String): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(date)
    }



}

