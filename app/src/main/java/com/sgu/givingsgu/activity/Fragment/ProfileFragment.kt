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
import com.sgu.givingsgu.utils.DataLocalManager
import com.sgu.givingsgu.utils.TokenUtils

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingUpListener()
    }

    override fun onResume() {
        super.onResume()
        init()
    }

    fun init() {
        // Retrieve user and display data
        val token = DataLocalManager.getToken()
        if (token != null) {
            val sub = TokenUtils.getClaimFromToken(token, "sub")
            binding.name.text = sub
            println("User sub: $sub")
            Toast.makeText(requireContext(), "Welcome back $sub", Toast.LENGTH_SHORT).show()
            // You can use the user data for further processing here
        } else {
            binding.name.text = "Guest"
            binding.textView26.text = "Log in"
            println("token is null")
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