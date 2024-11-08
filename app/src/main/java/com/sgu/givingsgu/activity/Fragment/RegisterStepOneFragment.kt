package com.sgu.givingsgu.activity.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.replace
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.sgu.givingsgu.R
import com.sgu.givingsgu.activity.RegistrationActivity
import com.sgu.givingsgu.databinding.FragmentRegisterStepOneBinding

class RegisterStepOneFragment : Fragment() {
    private var _binding: FragmentRegisterStepOneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterStepOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.submitBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.password.text.toString()

            if (name.isEmpty() || email.isEmpty()) {
                // Show an error message or a toast
                Toast.makeText(context, "Email and Password cannot be empty", Toast.LENGTH_SHORT).show()
            } else {
                val bundle = Bundle().apply {
                    putString("name", name)
                    putString("email", email)
                }

                val stepTwoFragment = RegisterStepTwoFragment().apply {
                    arguments = bundle
                }

                parentFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, stepTwoFragment)
                    .addToBackStack(null)
                    .commit()
                (activity as RegistrationActivity).findViewById<LinearProgressIndicator>(R.id.progressIndicator).progress = 100
                (activity as RegistrationActivity).findViewById<TextView>(R.id.textView).setText("STEP 2 OF 2")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}