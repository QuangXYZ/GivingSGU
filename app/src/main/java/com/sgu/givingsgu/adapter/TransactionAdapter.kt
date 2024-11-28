package com.sgu.givingsgu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.SingleRewardItemBinding
import com.sgu.givingsgu.databinding.SingleTransactionLayoutBinding
import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.response.TransactionResponse
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(val transactions: MutableList<TransactionResponse>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleTransactionLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        if (transactions[position].paymentMethod == "Momo"){
            Glide.with(context)
                .load(R.drawable.momo_square_pinkbg)
                .into(holder.binding.image)
        }
        else {
            Glide.with(context)
                .load(R.drawable.zalo_pay_logo)
                .into(holder.binding.image)
        }

        holder.binding.name.text = transactions[position].fullName
        holder.binding.amount.text = formatCurrency(transactions[position].amount)
        holder.binding.date.text = transactions[position].transactionDate
        holder.binding.status.text = transactions[position].status


    }

    override fun getItemCount(): Int = transactions.size

    class ViewHolder(val binding: SingleTransactionLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    fun formatCurrency(amount: Double): String {
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(amount)
        return "$formatted VND"
    }

}

