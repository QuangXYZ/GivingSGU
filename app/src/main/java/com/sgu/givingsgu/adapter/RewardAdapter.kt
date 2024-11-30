package com.sgu.givingsgu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.SingleRewardItemBinding
import com.sgu.givingsgu.model.Reward

class RewardAdapter (val rewards: MutableList<Reward>, val listener: OnRewardClickListener) :
    RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleRewardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (rewards[position].imageUrl.isNotEmpty()) {
            Glide.with(context).load(rewards[position].imageUrl).into(holder.binding.imageView10)
        }
        holder.binding.tvRewardName.text = rewards[position].name
        holder.binding.tvRewardPoint.text = rewards[position].pointsRequired.toString()+"RP"
        holder.binding.tvRewardQuantity.text = "Còn lại "+rewards[position].quantity.toString()

        if (position == selectedPosition) {
            holder.binding.root.strokeColor = context.getColor(R.color.yellow) // Màu khi được chọn
        } else {
            holder.binding.root.strokeColor = context.getColor(R.color.lightGrey)

        }


        holder.binding.root.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = holder.position
            notifyItemChanged(previousPosition) // Cập nhật trạng thái của item trước đó
            notifyItemChanged(selectedPosition) // Cập nhật trạng thái của item hiện tại
            listener.onRewardClick(rewards[position])
        }


    }

    override fun getItemCount(): Int = rewards.size

    class ViewHolder(val binding: SingleRewardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    interface OnRewardClickListener {
        fun onRewardClick(reward: Reward)
    }

}

