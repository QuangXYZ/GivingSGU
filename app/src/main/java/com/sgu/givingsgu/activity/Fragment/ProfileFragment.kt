package com.sgu.givingsgu.activity.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgu.givingsgu.activity.LoginActivity
import com.sgu.givingsgu.databinding.FragmentProfileBinding
import com.sgu.givingsgu.utils.DataLocalManager


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
        init()
        settingUpListener()
    }




    fun init() {
        // init
//        binding.loginButton.setOnClickListener(View.OnClickListener {
//            val intent = Intent(requireContext(), LoginActivity::class.java)
//            // Bắt đầu Activity mới
//            startActivity(intent)
//        })

    }
    fun settingUpListener() {
        binding.logout.setOnClickListener(View.OnClickListener {
            DataLocalManager.setLoggedIn(false)
            DataLocalManager.clearTokenAndUser()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            // Bắt đầu Activity mới
            startActivity(intent)
        })
    }
}