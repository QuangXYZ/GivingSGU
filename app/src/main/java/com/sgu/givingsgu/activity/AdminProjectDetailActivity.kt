package com.sgu.givingsgu.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.adapter.CommentAdapter
import com.sgu.givingsgu.adapter.DonorAdapter
import com.sgu.givingsgu.adapter.ImageAdapter
import com.sgu.givingsgu.databinding.ActivityAdminProjectDetailBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.viewmodel.ProjectDetailViewModel
import java.text.NumberFormat
import java.util.Date
import java.util.Locale
import com.sgu.givingsgu.R
import java.util.concurrent.TimeUnit

class AdminProjectDetailActivity : BaseActivity() {
    private lateinit var binding: ActivityAdminProjectDetailBinding
    private lateinit var  commentAdapter: CommentAdapter
    private lateinit var donorAdapter: DonorAdapter
    private lateinit var project: Project
    private lateinit var viewModel: ProjectDetailViewModel
    private lateinit var images: List<String>
    private lateinit var imageAdapter: ImageAdapter

    var isExpanded: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminProjectDetailBinding.inflate(layoutInflater)
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

        binding.adminProjectName.text = project.name
        binding.adminProjectDescription.text = project.description
        if (project.imageUrls != null) {
//            if (project.imageUrls?.split(",")?.toTypedArray()?.get(0) != null) {
//                val img = project.imageUrls?.split(",")?.toTypedArray()
//                Glide.with(this).load(img?.get(0)).into(binding.adminProjectImg)
//            }
            images = project.imageUrls?.split(",")?.toList()!!

            Glide.with(this).load(images[0]).into(binding.adminProjectImg)
        }
//        binding.adminProjectAmount.text = project.currentAmount.toString() + " VND"
        binding.adminProjectAmount.text = project.currentAmount?.let { formatCurrency(it) }
        val percent = (project.currentAmount?.div(project.targetAmount)
            ?.times(100))
            ?.toInt()
        binding.adminProjectPercent.text = percent.toString() + "%"
        binding.adminProjectProgress.progress = percent!!
        binding.adminProjectTime.text = calculateTimeRemaining(project.endDate)
//        binding.adminProjectTarget.text = project.targetAmount.toString() + " VND"
        binding.adminProjectTarget.text = formatCurrency(project.targetAmount)


        val fullText = project.description
        binding.adminProjectDescription.text = fullText
        // Kiểm tra nếu nội dung quá dài thì mặc định chỉ hiển thị 3 dòng
        binding.adminProjectDescription.post(Runnable {
            if (binding.adminProjectDescription.getLineCount() > 4) {
                binding.adminProjectDescription.setMaxLines(4)
                binding.adminProjectDescription.ellipsize = TextUtils.TruncateAt.END
            }
        })

        initComment()
        initTopDonor()
        initImage()
    }
    fun settingUpListener() {
        binding.adminBackBtn.setOnClickListener {
            finish()
        }

        binding.adminProjectDescription.setOnClickListener(object : View.OnClickListener {

            override fun onClick(v: View) {
                if (isExpanded) {
                    // Nếu đang mở rộng, thu gọn văn bản lại
                    binding.adminProjectDescription.setMaxLines(4)
                    binding.adminProjectDescription.setEllipsize(TextUtils.TruncateAt.END)
                } else {
                    // Nếu đang thu gọn, mở rộng văn bản ra
                    binding.adminProjectDescription.setMaxLines(Int.MAX_VALUE)
                    binding.adminProjectDescription.setEllipsize(null)
                }
                isExpanded = !isExpanded
            }
        })
        binding.adminDonateBtn.setOnClickListener {
            val intent = Intent(this, UpdateProjectActivity::class.java)
            intent.putExtra("project", project)
            startActivity(intent)
        }
    }

    private fun initComment() {
        viewModel.comment.observe(this, Observer {
            commentAdapter = CommentAdapter(it.toMutableList())
            binding.adminCommentRecyclerView.adapter = commentAdapter
            binding.adminCommentRecyclerView.isNestedScrollingEnabled = true
            binding.adminCommentRecyclerView.layoutManager = LinearLayoutManager(this)
        })
        project?.projectId?.let { viewModel.fetchAllComment(it) }
    }
    private fun initTopDonor() {
        viewModel.donation.observe(this, Observer {

            if (it.size >= 1) {
                if (it[0].imageUrl != null) {
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
                if (it[1].imageUrl != null) {
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
                if (it[2].imageUrl != null) {
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
            binding.adminTopDonorRecyclerView.adapter = donorAdapter
            binding.adminTopDonorRecyclerView.isNestedScrollingEnabled = true
            binding.adminTopDonorRecyclerView.layoutManager = LinearLayoutManager(this)
        })
        project?.projectId?.let { viewModel.fetchTopDonors(it) }
    }

    private fun initImage() {
        images = project.imageUrls?.split(",") ?: listOf("https://www.bluemoongame.com/wp-content/uploads/2021/08/002-Unwanted-Experiment.jpg","https://www.bluemoongame.com/wp-content/uploads/2021/08/002-Unwanted-Experiment.jpg","https://www.bluemoongame.com/wp-content/uploads/2021/08/002-Unwanted-Experiment.jpg")

        imageAdapter = ImageAdapter(images)

        binding.imageRecyclerView.adapter = imageAdapter
        binding.imageRecyclerView.isNestedScrollingEnabled = true
        binding.imageRecyclerView.layoutManager =   LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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