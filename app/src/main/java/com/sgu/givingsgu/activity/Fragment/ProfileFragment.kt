package com.sgu.givingsgu.activity.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sgu.givingsgu.activity.LoginActivity
import com.sgu.givingsgu.activity.MainActivity
import com.sgu.givingsgu.databinding.FragmentProfileBinding
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.utils.DataLocalManager



class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpListener()
        init()
    }

    override fun onResume() {
        super.onResume()

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
    }
}