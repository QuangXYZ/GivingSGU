package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.adapter.CommentAdapter
import com.sgu.givingsgu.adapter.DonorAdapter
import com.sgu.givingsgu.adapter.ProjectAdapter
import com.sgu.givingsgu.databinding.ActivityProjectDetailBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.viewmodel.ProjectDetailViewModel
import java.util.Date
import java.util.concurrent.TimeUnit


class ProjectDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProjectDetailBinding
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var donorAdapter: DonorAdapter

    private lateinit var project : Project
    private lateinit var viewModel: ProjectDetailViewModel

    var isExpanded: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        settingUpListener()

        }


    fun init() {
        viewModel = ProjectDetailViewModel()
        // get bundle
        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("project", Project::class.java)!!
        } else {
            intent.getParcelableExtra("project")!!
        }

        initComment()
        initTopDonor()

        binding.projectName.text = project.name
        binding.projectDescription.text = project.description
        if (project.imageUrls != null) {
            if (project.imageUrls?.split(",")?.toTypedArray()?.get(0) != null) {
                val img = project.imageUrls?.split(",")?.toTypedArray()
                Glide.with(this).load(img?.get(0)).into(binding.projectImg)
            }
        }
        binding.projectAmount.text = project.currentAmount.toString() + " VND"
        val percent = (project.currentAmount?.div(project.targetAmount)
            ?.times(100))
            ?.toInt()
        binding.projectPercent.text = percent.toString() + "%"
        binding.projectProgress.progress = percent!!
        binding.projectTime.text = calculateTimeRemaining(project.endDate)
        binding.projectTarget.text = project.targetAmount.toString() + " VND"



        val fullText = project.description
        binding.projectDescription.setText(fullText)
        // Kiểm tra nếu nội dung quá dài thì mặc định chỉ hiển thị 3 dòng
        binding.projectDescription.post(Runnable {
            if (binding.projectDescription.getLineCount() > 4) {
                binding.projectDescription.setMaxLines(4)
                binding.projectDescription.setEllipsize(TextUtils.TruncateAt.END)
            }
        })
    }
    fun settingUpListener() {
        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.projectDescription.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View) {
                if (isExpanded) {
                    // Nếu đang mở rộng, thu gọn văn bản lại
                    binding.projectDescription.setMaxLines(4)
                    binding.projectDescription.setEllipsize(TextUtils.TruncateAt.END)
                } else {
                    // Nếu đang thu gọn, mở rộng văn bản ra
                    binding.projectDescription.setMaxLines(Int.MAX_VALUE)
                    binding.projectDescription.setEllipsize(null)
                }
                isExpanded = !isExpanded
            }
        })
        binding.donateBtn.setOnClickListener {
            val intent = Intent(this, DonationActivity::class.java)
            intent.putExtra("project", project)
            startActivity(intent)
        }
    }

    private fun initComment() {
        viewModel.comment.observe(this, Observer {
            commentAdapter = CommentAdapter(it.toMutableList())
            binding.commentRecyclerView.adapter = commentAdapter
            binding.commentRecyclerView.isNestedScrollingEnabled = true
            binding.commentRecyclerView.layoutManager = LinearLayoutManager(this)
        })
        project?.projectId?.let { viewModel.fetchAllComment(it) }
    }
    private fun initTopDonor() {
        viewModel.donation.observe(this, Observer {
            donorAdapter = DonorAdapter(it.toMutableList())
            binding.topDonorRecyclerView.adapter = donorAdapter
            binding.topDonorRecyclerView.isNestedScrollingEnabled = true
            binding.topDonorRecyclerView.layoutManager = LinearLayoutManager(this)
        })
        project?.projectId?.let { viewModel.fetchTopDonors(it) }
    }


    fun calculateTimeRemaining(endDate: Date): String {
        val currentDate = Date()
        val diffInMillis = endDate.time - currentDate.time

        return if (diffInMillis < 0) {
            "Đã kết thúc"
        } else {
            val diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS)
            val diffInHours = TimeUnit.HOURS.convert(diffInMillis, TimeUnit.MILLISECONDS) % 24
            val diffInMinutes = TimeUnit.MINUTES.convert(diffInMillis, TimeUnit.MILLISECONDS) % 60

            // Kiểm tra nếu số ngày < 1 thì chỉ hiển thị giờ
            if (diffInDays < 1) {
                "$diffInHours giờ, $diffInMinutes phút"
            } else {
                "$diffInDays ngày, $diffInHours giờ"
            }
        }
    }
}