package com.sgu.givingsgu.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.activity.AdminProjectDetailActivity
import com.sgu.givingsgu.activity.ProjectDetailActivity
import com.sgu.givingsgu.databinding.AdminSingleProjectHorizontalLayoutBinding
import com.sgu.givingsgu.model.Project
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class AdminProjectHighLightAdapter(val projects: MutableList<Project>) :
    RecyclerView.Adapter<AdminProjectHighLightAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            AdminSingleProjectHorizontalLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = projects[position].name
        holder.binding.donateAmount.text = projects[position].currentAmount.toString() + " VND"
        val percent = (projects[position].currentAmount?.div(projects[position].targetAmount)
            ?.times(100))
            ?.toInt()
        holder.binding.donatePercent.text = percent.toString() + "%"
        holder.binding.linearProgressIndicator.progress = percent!!

        if (projects[position].imageUrls != null) {
            if (projects[position].imageUrls?.split(",")?.toTypedArray()?.get(0) != null) {
                val img = projects[position].imageUrls?.split(",")?.toTypedArray()
                Glide.with(context).load(img?.get(0)).into(holder.binding.imageView2)
            }

        }

        holder.binding.donateNumber.text =
            projects[position].numberDonors.toString() + " người đã ủng hộ"
        holder.binding.projectTime.text =
            formatTimeDifference(projects[position].startDate!!).toString()

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, AdminProjectDetailActivity::class.java)
            intent.putExtra("project", projects[position])
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = projects.size

    class ViewHolder(val binding: AdminSingleProjectHorizontalLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    fun formatTimeDifference(startDate: Date): String {
        // Chuyển đổi Date sang LocalDateTime
        val startDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val currentDateTime = LocalDateTime.now() // Ngày và giờ hiện tại

        // Tính số tháng, ngày và giờ
        val months = ChronoUnit.MONTHS.between(startDateTime, currentDateTime)
        val days = ChronoUnit.DAYS.between(startDateTime, currentDateTime) % 30 // Số ngày còn lại
        val hours = ChronoUnit.HOURS.between(startDateTime, currentDateTime) % 24 // Số giờ còn lại

        // Xác định định dạng cho chuỗi đầu ra
        return when {
            months > 0 -> "$months tháng trước"
            days > 0 -> "$days ngày trước"
            hours > 0 -> "$hours giờ trước"
            else -> "Vừa mới"
        }
    }

}


