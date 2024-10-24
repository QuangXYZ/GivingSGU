package com.sgu.givingsgu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sgu.givingsgu.databinding.AdminSingleProjectLayoutBinding
import com.sgu.givingsgu.model.Project
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date

class AdminProjectAdapter(val projects: MutableList<Project>) :
    RecyclerView.Adapter<AdminProjectAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            AdminSingleProjectLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.adminTitle.text = projects[position].name
        holder.binding.adminDonateAmount.text = projects[position].currentAmount.toString() + " VND"
        val percent = (projects[position].currentAmount?.div(projects[position].targetAmount)
            ?.times(100))
            ?.toInt()
        holder.binding.adminDonatePercent.text = percent.toString() + "%"
        holder.binding.adminLinearProgressIndicator.progress = percent!!

        holder.binding.adminDonateNumber.text =
            projects[position].numberDonors.toString() + " người đã ủng hộ"
        holder.binding.projectTime.text =
            formatTimeDifference(projects[position].startDate!!).toString()

//        holder.itemView.setOnClickListener {
//            val intent = Intent(holder.itemView.context, ProjectDetailActivity::class.java)
////            intent.putExtra("object", projects[position])
//            holder.itemView.context.startActivity(intent)
//        }
    }


    override fun getItemCount(): Int = projects.size

    class ViewHolder(val binding: AdminSingleProjectLayoutBinding) :
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


