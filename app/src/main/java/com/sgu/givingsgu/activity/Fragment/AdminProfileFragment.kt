package com.sgu.givingsgu.activity.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.sgu.givingsgu.activity.LoginActivity
import com.sgu.givingsgu.activity.MainActivity
import com.sgu.givingsgu.databinding.FragmentAdminProfileBinding
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.repository.RewardRepository
import com.sgu.givingsgu.utils.CaptureArt
import com.sgu.givingsgu.utils.DataLocalManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdminProfileFragment : Fragment() {
    private lateinit var binding: FragmentAdminProfileBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 100


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        settingUpListener()
    }



    fun init() {
        val user: User? = DataLocalManager.getUser()
        if (user != null) {
            binding.name.text = user.username
            binding.btn.text = "Log out"
            binding.studentid.text = user.studentId
            binding.faculty.text = when (user.facultyId.toInt()) {
                1 -> "Information Technology"
                2 -> "Education"
                3 -> "Banking"
                4 -> "Tourism and Tour Guiding"
                else -> "Other Faculty"
            }
            binding.points.text = user.points.toString()
            binding.email.text = user.email
            binding.phone.text = user.phoneNumber
            binding.fullname.text = user.fullName
            Toast.makeText(requireContext(), "Welcome back ${user.username}", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Welcome Guess", Toast.LENGTH_SHORT).show()
        }
    }

    fun settingUpListener() {
        binding.logout.setOnClickListener(View.OnClickListener {
            if(DataLocalManager.getToken() != null) {
                DataLocalManager.setLoggedIn(false)
                DataLocalManager.clearTokenAndUser()
                val intent2 = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent2)
            }
            else {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
            }
        })

        binding.qrscan.setOnClickListener(View.OnClickListener {
            if (isCameraPermissionGranted()) {
                // Quản lý quét QR
                initQRCodeScanner()
            } else {
                requestCameraPermission()
            }
        })

    }

    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    // Yêu cầu quyền camera nếu chưa được cấp
    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    // Xử lý kết quả khi người dùng cho phép hoặc từ chối quyền
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền được cấp, thực hiện quét QR
                initQRCodeScanner()
            } else {
                // Quyền bị từ chối, thông báo cho người dùng
                Toast.makeText(requireContext(), "Camera permission is required to scan QR codes", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun initQRCodeScanner() {
        val integrator = IntentIntegrator(requireActivity())
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setOrientationLocked(true)
        integrator.setCaptureActivity(CaptureArt::class.java)
        integrator.setPrompt("Scan a QR code")
        integrator.initiateScan()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                Toast.makeText(requireContext(), "QR Code: ${result.contents}", Toast.LENGTH_LONG).show()
                val rewardRepository = RewardRepository()
                rewardRepository.confirmReward(result.contents).enqueue(object :
                    Callback<ResponseWrapper<String>> {
                    override fun onResponse(
                        call: Call<ResponseWrapper<String>>,
                        response: Response<ResponseWrapper<String>>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Reward confirmed", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Failed to confirm reward", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseWrapper<String>>, t: Throwable) {
                        Toast.makeText(requireContext(), "Failed to confirm reward", Toast.LENGTH_SHORT).show()
                    }
                })

            } else {
                Toast.makeText(requireContext(), "Scan cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}