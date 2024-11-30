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
import com.sgu.givingsgu.R
import com.sgu.givingsgu.adapter.CommentAdapter
import com.sgu.givingsgu.adapter.DonorAdapter
import com.sgu.givingsgu.adapter.ImageAdapter
import com.sgu.givingsgu.databinding.ActivityProjectDetailBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.viewmodel.ProjectDetailViewModel
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


class ProjectDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityProjectDetailBinding
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var donorAdapter: DonorAdapter
    private lateinit var imageAdapter: ImageAdapter


    private lateinit var project: Project
    private lateinit var viewModel: ProjectDetailViewModel
    private lateinit var images: List<String>


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



        binding.projectName.text = project.name
        binding.projectDescription.text = project.description
        if (project.imageUrls != null) {
            images = project.imageUrls?.split(",")?.toList()!!

            Glide.with(this).load(images[0]).into(binding.projectImg)
        }
        val percent = (project.currentAmount?.div(project.targetAmount)
            ?.times(100))
            ?.toInt()
        binding.projectPercent.text = percent.toString() + "%"
        binding.projectProgress.progress = percent!!
        binding.projectTime.text = calculateTimeRemaining(project.endDate)

        binding.projectTarget.text = formatCurrency(project.targetAmount)

        binding.projectAmount.text = project.currentAmount?.let { formatCurrency(it) }
        val fullText = project.description
        binding.projectDescription.text = fullText
        // Kiểm tra nếu nội dung quá dài thì mặc định chỉ hiển thị 3 dòng
        binding.projectDescription.post(Runnable {
            if (binding.projectDescription.lineCount > 4) {
                binding.projectDescription.setMaxLines(4)
                binding.projectDescription.ellipsize = TextUtils.TruncateAt.END
            }
        })

        initComment()
        initTopDonor()
        initImage()


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
                    binding.projectDescription.ellipsize = TextUtils.TruncateAt.END
                } else {
                    // Nếu đang thu gọn, mở rộng văn bản ra
                    binding.projectDescription.setMaxLines(Int.MAX_VALUE)
                    binding.projectDescription.ellipsize = null
                }
                isExpanded = !isExpanded
            }
        })
        binding.donateBtn.setOnClickListener {
            val intent = Intent(this, DonationActivity::class.java)
            intent.putExtra("project", project)
            startActivity(intent)
        }

        binding.transactionHistory.setOnClickListener {
            val intent = Intent(this, DonationHistoryActivity::class.java)
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
        project.projectId.let { viewModel.fetchAllComment(it) }
    }

    private fun initTopDonor() {
        viewModel.donation.observe(this, Observer {

            if (it.size >= 1) {
                if (it[0].imageUrl != null && it[0].imageUrl != "") {
                    Glide.with(this)
                        .load(it[0].imageUrl)
                        .centerInside()
                        .into(binding.firstUserImg)
                } else {
                    binding.firstUserImg.setImageResource(R.drawable.user_default)
                }
                binding.firstUserName.text = it[0].fullName
                binding.firstUserAmount.text = formatCurrency(it[0].totalAmount)
            }

            if (it.size >= 2) {
                if (it[1].imageUrl != null && it[1].imageUrl != "") {
                    Glide.with(this)
                        .load(it[1].imageUrl)
                        .centerInside()
                        .into(binding.secondUserImg)
                } else {
                    binding.secondUserImg.setImageResource(R.drawable.user_default)
                }
                binding.secondUserName.text = it[1].fullName
                binding.secondUserAmount.text = formatCurrency(it[1].totalAmount)
            }

            if (it.size >= 3) {
                if (it[2].imageUrl != null && it[2].imageUrl != "") {
                    Glide.with(this)
                        .load(it[2].imageUrl)
                        .centerInside()
                        .into(binding.thirdUserImg)
                } else {
                    binding.thirdUserImg.setImageResource(R.drawable.user_default)
                }
                binding.thirdUserName.text = it[2].fullName
                binding.thirdUserAmount.text = formatCurrency(it[2].totalAmount)

            }


            donorAdapter = DonorAdapter(it.drop(3).toMutableList())
            binding.topDonorRecyclerView.adapter = donorAdapter
            binding.topDonorRecyclerView.isNestedScrollingEnabled = true
            binding.topDonorRecyclerView.layoutManager = LinearLayoutManager(this)
        })
        project.projectId.let { viewModel.fetchTopDonors(it) }
    }

    private fun initImage() {
        images = project.imageUrls?.split(",") ?: listOf(
            "https://www.bluemoongame.com/wp-content/uploads/2021/08/002-Unwanted-Experiment.jpg",
            "https://www.bluemoongame.com/wp-content/uploads/2021/08/002-Unwanted-Experiment.jpg",
            "https://www.bluemoongame.com/wp-content/uploads/2021/08/002-Unwanted-Experiment.jpg"
        )

        imageAdapter = ImageAdapter(images)

        binding.imageRecyclerView.adapter = imageAdapter
        binding.imageRecyclerView.isNestedScrollingEnabled = true
        binding.imageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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


    fun formatCurrency(amount: Double): String {
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(amount)
        return "$formatted VND"
    }

}



