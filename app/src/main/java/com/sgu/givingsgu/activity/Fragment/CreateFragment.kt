package com.sgu.givingsgu.activity.Fragment

import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import android.Manifest
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.sgu.givingsgu.adapter.ImagePreviewAdapter
import com.sgu.givingsgu.databinding.FragmentCreateBinding
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.ProjectDTO
import com.sgu.givingsgu.viewmodel.AdminHomeViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CreateFragment : Fragment() {
    private lateinit var binding: FragmentCreateBinding
    private lateinit var imagePreviewAdapter: ImagePreviewAdapter
    private val viewModel: AdminHomeViewModel by viewModels()
    private val PERMISSION_REQUEST_CODE = 1001


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
                    context,
                    "Số lượng ảnh đã chọn: ${imageUris.size}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(context, "Không có ảnh nào được chọn", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tạo adapter và truyền callback xóa ảnh
        imagePreviewAdapter = ImagePreviewAdapter(imageUris) { uri ->
            // Xử lý khi xóa ảnh khỏi danh sách
            imageUris.remove(uri)
            imagePreviewAdapter.updateImages(imageUris)
        }

        binding.imagesRecyclerview.adapter = imagePreviewAdapter
        binding.imagesRecyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Xử lý chọn ngày bắt đầu
        binding.projectStartDate.setOnClickListener {
            showDatePicker { date ->
                binding.projectStartDate.setText(date)
            }
        }

        // Xử lý chọn ngày kết thúc
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

        // Xử lý khi submit dự án
        binding.submitButton.setOnClickListener {
            if (validateFields()) {
                val project = createProjectFromInputs()
                viewModel.uploadImagesAndCreateProject(imageUris, project)
            }
        }

        // Lắng nghe kết quả từ ViewModel
        viewModel.creationResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(context, "Tạo dự án thành công!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "Tạo dự án thất bại: ${result.isFailure}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val date = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                onDateSelected(date)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun createProjectFromInputs(): ProjectDTO {
        return ProjectDTO(
            name = binding.projectName.text.toString(),
            description = binding.projectDescription.text.toString(),
            startDate = binding.projectStartDate.text.toString(),
            endDate = binding.projectEndDate.text.toString(),
            targetAmount = binding.projectTargetAmount.text.toString().toDouble(),
            currentAmount = 0.0,
            numberDonors = 0,
            status = "Đang diễn ra",
            managedBy = 4L  // Giả sử ID người quản lý là 123, có thể lấy từ `SharedPreferences`
        )
    }

    private fun checkPermissions(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                PERMISSION_REQUEST_CODE
            )
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, cho phép người dùng chọn ảnh
                imagePickerLauncher.launch("image/*")
            } else {
                // Quyền bị từ chối
                Toast.makeText(context, "Quyền truy cập ảnh bị từ chối", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        val name = binding.projectName.text.toString().trim()
        val description = binding.projectDescription.text.toString().trim()
        val startDate = binding.projectStartDate.text.toString().trim()
        val endDate = binding.projectEndDate.text.toString().trim()
        val targetAmount = binding.projectTargetAmount.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(context, "Tên dự án không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        if (description.isEmpty()) {
            Toast.makeText(context, "Mô tả dự án không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        if (startDate.isEmpty()) {
            Toast.makeText(context, "Ngày bắt đầu không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        if (endDate.isEmpty()) {
            Toast.makeText(context, "Ngày kết thúc không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        if (targetAmount.isEmpty()) {
            Toast.makeText(context, "Số tiền mục tiêu không được để trống", Toast.LENGTH_SHORT).show()
            return false
        }

        if (imageUris.isEmpty()) {
            Toast.makeText(context, "Bạn phải chọn ít nhất một ảnh", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}