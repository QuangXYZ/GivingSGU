package com.sgu.givingsgu.activity

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.quang.lilyshop.activity.BaseActivity
import com.sgu.givingsgu.R
import com.sgu.givingsgu.adapter.ImagePreviewAdapter
import com.sgu.givingsgu.databinding.ActivityUpdateProjectBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.ProjectDTO
import com.sgu.givingsgu.viewmodel.UpdateProjectViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class UpdateProjectActivity : BaseActivity() {
    private lateinit var binding: ActivityUpdateProjectBinding
    private lateinit var project: Project
    private lateinit var imagePreviewAdapter: ImagePreviewAdapter
    private val viewModel: UpdateProjectViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setupSpinner()
        displayProjectData()
        settingUpListener()
    }


    private fun init() {
        project = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("project", Project::class.java)!!
        } else {
            intent.getParcelableExtra("project")!!
        }
    }

    private fun setupSpinner() {
        val adapter = object : ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.status_options)
        ) {
            override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getView(position, convertView, parent)
                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.black)) // Thiết lập màu chữ đen cho mục đã chọn
                return view
            }

            override fun getDropDownView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
                val view = super.getDropDownView(position, convertView, parent)
//                (view as TextView).setTextColor(ContextCompat.getColor(context, R.color.white)) // Thiết lập màu chữ đen cho mục trong dropdown
                return view
            }
        }

        // Thiết lập dropdown layout cho spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.projectStatus.adapter = adapter
        binding.projectStatus.setSelection(getStatusIndex(project.status))
    }

    private fun displayProjectData() {
        binding.projectName.setText(project.name)
        binding.projectDescription.setText(project.description)
        binding.projectStartDate.setText(formatDate(project.startDate))
        binding.projectEndDate.setText(formatDate(project.endDate))
        binding.projectTargetAmount.setText(project.targetAmount.toString())
        binding.projectStatus.setSelection(getStatusIndex(project.status))
        // Hiển thị ảnh đã chọn nếu có
        // Giả sử có ImagePreviewAdapter để hiển thị danh sách ảnh
    }

    private val imageUris = mutableListOf<Uri>()

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (!uris.isNullOrEmpty()) {
                for (uri in uris) {
                    Log.d("CreateFragment", "URI đã chọn: $uri")
                }
                imageUris.clear()
                imageUris.addAll(uris)
                imagePreviewAdapter.updateImages(imageUris)
                Toast.makeText(
                    this,
                    "Số lượng ảnh đã chọn: ${imageUris.size}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show()
            }
        }

    private fun settingUpListener() {
        // Tạo adapter và truyền callback xóa ảnh
        imagePreviewAdapter = ImagePreviewAdapter(imageUris) { uri ->
            // Xử lý khi xóa ảnh khỏi danh sách
            imageUris.remove(uri)
            imagePreviewAdapter.updateImages(imageUris)
        }

        binding.imagesRecyclerview.adapter = imagePreviewAdapter
        binding.imagesRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Chọn ngày bắt đầu
        binding.projectStartDate.setOnClickListener {
            showDatePicker { date ->
                binding.projectStartDate.setText(date)
            }
        }

        // Chọn ngày kết thúc
        binding.projectEndDate.setOnClickListener {
            showDatePicker { date ->
                binding.projectEndDate.setText(date)
            }
        }

        // Kiểm tra và yêu cầu quyền trước khi cho phép chọn ảnh
        binding.addImageButton.setOnClickListener {
            if (checkPermissions()) {
                // Nếu quyền đã được cấp, tiếp tục cho phép người dùng chọn ảnh
                imagePickerLauncher.launch("image/*")
            } else {
                // Nếu chưa có quyền, yêu cầu quyền
                requestPermissions()
            }
        }

        // Nút lưu cập nhật
        binding.submitButton.setOnClickListener {
            if (validateFields()) {
                val updatedProjectDTO = createProjectDTOFromInputs()
                viewModel.uploadImagesAndUpdateProject(project.projectId, imageUris, updatedProjectDTO)
            }
        }

        viewModel.updateResult.observe(this) { result ->
            if (result.isSuccess) {
                Toast.makeText(this, "Cập nhật án thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "Cập nhật án thất bại: ${result.isFailure}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createProjectDTOFromInputs(): ProjectDTO {
        return ProjectDTO(
            projectId = project.projectId,
            name = binding.projectName.text.toString(),
            description = binding.projectDescription.text.toString(),
            startDate = binding.projectStartDate.text.toString(),
            endDate = binding.projectEndDate.text.toString(),
            targetAmount = binding.projectTargetAmount.text.toString().toDouble(),
            status = binding.projectStatus.selectedItem.toString(),
            managedBy = project.managedBy,
            currentAmount = project.currentAmount,
            numberDonors = project.numberDonors,
            imageUrls = project.imageUrls
        )
    }

    private fun validateFields(): Boolean {
        val name = binding.projectName.text.toString().trim()
        val description = binding.projectDescription.text.toString().trim()
        val startDate = binding.projectStartDate.text.toString().trim()
        val endDate = binding.projectEndDate.text.toString().trim()
        val targetAmount = binding.projectTargetAmount.text.toString().trim()

        if (name.isEmpty() || description.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || targetAmount.isEmpty()) {
            Toast.makeText(this, "Tất cả các trường là bắt buộc", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                // Đảm bảo tháng và ngày đều có hai chữ số
                val formattedMonth = String.format("%02d", selectedMonth + 1)
                val formattedDay = String.format("%02d", selectedDay)
                val date = "$selectedYear-$formattedMonth-$formattedDay"
                onDateSelected(date)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(date)
    }

    private fun getStatusIndex(status: String?): Int {
        // Trả về chỉ mục phù hợp cho Spinner status
        val statuses = resources.getStringArray(R.array.status_options)
        return statuses.indexOf(status ?: "Đang diễn ra")
    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }
}