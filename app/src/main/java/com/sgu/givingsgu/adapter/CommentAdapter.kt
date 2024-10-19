package com.sgu.givingsgu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.R
import com.sgu.givingsgu.databinding.SingleCommentLayoutBinding
import com.sgu.givingsgu.model.Comment
import com.sgu.givingsgu.network.response.CommentResponse

class CommentAdapter(val comments: MutableList<CommentResponse>) :
    RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleCommentLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.comment.text = comments[position].content
        holder.binding.name.text = comments[position].userId.toString()
        holder.binding.timestamp.text = comments[position].projectId.toString()
        holder.binding.name.text = comments[position].fullName


        if (comments[position].imageUrl!=null) {
            Glide.with(context)
                .load(comments[position].imageUrl)
                .centerInside()
                .into(holder.binding.image)
        }
        else {
            holder.binding.image.setImageResource(R.drawable.user_default)
        }

    }


    override fun getItemCount(): Int = comments.size

    class ViewHolder(val binding: SingleCommentLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


}


