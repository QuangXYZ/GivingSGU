package com.sgu.givingsgu.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgu.givingsgu.databinding.ItemImageBinding

class ImagePreviewAdapter(
    private var imageUris: List<Uri>,
    private val onRemoveClick: (Uri) -> Unit
) : RecyclerView.Adapter<ImagePreviewAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.selectedImage.setImageURI(uri)
            // Nút xóa ảnh
            binding.removeImageButton.setOnClickListener {
                onRemoveClick(uri)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageUris[position])
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    fun updateImages(newImageUris: List<Uri>) {
        imageUris = newImageUris
        notifyDataSetChanged()
    }
}
