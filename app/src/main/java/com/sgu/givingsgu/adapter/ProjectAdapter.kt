package com.sgu.givingsgu.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sgu.givingsgu.activity.ProjectDetailActivity
import com.sgu.givingsgu.databinding.SingleProjectLayoutBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.repository.ProjectRepository
import com.sgu.givingsgu.utils.DataLocalManager
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ProjectAdapter(val projects: MutableList<ProjectResponse>) : RecyclerView.Adapter<ProjectAdapter.ViewHolder>() {

    private lateinit var context: Context

    private var projectRepository = ProjectRepository()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding =
            SingleProjectLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = projects[position].project.name

        val cleanString = projects[position].project.currentAmount.toString().replace("""[,.]""".toRegex(), "")

        // Chuyển chuỗi về dạng số
        val parsed = cleanString.toDoubleOrNull() ?: 0.0

        // Định dạng số theo kiểu tiền tệ
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(parsed)
        holder.binding.donateAmount.text = formatted+ " VND"
        val percent = (projects[position].project.currentAmount?.div(projects[position].project.targetAmount)
            ?.times(100))
            ?.toInt()
        holder.binding.donatePercent.text = percent.toString()+"%"
        holder.binding.linearProgressIndicator.progress = percent!!

        if (projects[position].project.imageUrls != null) {
            if (projects[position].project.imageUrls?.split(",")?.toTypedArray()?.get(0) != null) {
                val img = projects[position].project.imageUrls?.split(",")?.toTypedArray()
                Glide.with(context).load(img?.get(0)).into(holder.binding.projectImg)
            }

        }

        holder.binding.donateNumber.text = projects[position].project.numberDonors.toString()+" người đã ủng hộ"
        holder.binding.projectTime.text = formatTimeDifference(projects[position].project.startDate!!)
        if (projects[position].liked) {
            holder.binding.homeLikeBtn.isChecked = true
        }

        holder.binding.likeCount.text = projects[position].likeCount.toString()

        holder.binding.homeLikeBtn.setOnClickListener {
            if (!DataLocalManager.isLoggedIn()) return@setOnClickListener
            if(holder.binding.homeLikeBtn.isChecked) {
                projectRepository.unlikeProject(projects[position].project.projectId)
                holder.binding.likeCount.text = (projects[position].likeCount - 1).toString()


            }
            else {
                projectRepository.likeProject(projects[position].project.projectId)
                holder.binding.likeCount.text = (projects[position].likeCount + 1).toString()

            }


        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ProjectDetailActivity::class.java)
            intent.putExtra("project", projects[position].project)
            holder.itemView.context.startActivity(intent)
        }

    }


    override fun getItemCount(): Int = projects.size

    class ViewHolder(val binding: SingleProjectLayoutBinding) :
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


