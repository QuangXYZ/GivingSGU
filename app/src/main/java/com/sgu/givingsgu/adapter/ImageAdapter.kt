package com.sgu.givingsgu.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.databinding.SingleImageviewLayoutBinding
import com.stfalcon.imageviewer.StfalconImageViewer

class ImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleImageviewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (images[position]!=null) {
            Glide.with(context)
                .load(images[position])
                .centerInside()
                .into(holder.binding.imageView)
        }

        holder.binding.root.setOnClickListener(){
            StfalconImageViewer.Builder(holder.itemView.context, images) { view, image ->
                Glide.with(holder.itemView.context).load(image).into(view)
            }
                .withStartPosition(position)  // Bắt đầu từ vị trí ảnh được nhấn
                .withHiddenStatusBar(false)
                .show()
        }

    }

    override fun getItemCount(): Int = images.size

    class ViewHolder(val binding: SingleImageviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }


}


