package com.sgu.givingsgu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.SingleCommentLayoutBinding
import com.sgu.givingsgu.databinding.SingleDonorLayoutBinding
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.network.response.DonationResponse
import java.text.NumberFormat
import java.util.Locale

class DonorAdapter (val donations: MutableList<DonationResponse>) :
    RecyclerView.Adapter<DonorAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleDonorLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.donorName.text = donations[position].fullName
        holder.binding.donorDate.text = donations[position].totalAmount.toString()


        val cleanString = donations[position].totalAmount.toString().replace("""[,.]""".toRegex(), "")

        // Chuyển chuỗi về dạng số
        val parsed = cleanString.toDoubleOrNull() ?: 0.0

        val formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed)
        holder.binding.donationAmount.text = formatted+ " VND"

        if (position%2==0) {
            holder.binding.materialCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.orange_100))
        }
        else {
            holder.binding.materialCardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.teal_100))
        }

        if (donations[position].imageUrl!=null) {
            Glide.with(context)
                .load(donations[position].imageUrl)
                .centerInside()
                .into(holder.binding.circleImageView2)
        }
        else {
            holder.binding.circleImageView2.setImageResource(R.drawable.user_default)
        }

    }

    override fun getItemCount(): Int = donations.size

    class ViewHolder(val binding: SingleDonorLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


}


