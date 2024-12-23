package com.sgu.givingsgu.activity.Fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.sgu.givingsgu.databinding.FragmentRegisterStepTwoBinding
import com.sgu.givingsgu.viewmodel.RegistrationViewModel

class RegisterStepTwoFragment : Fragment() {
    private var _binding: FragmentRegisterStepTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegistrationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterStepTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)

        val faculties = listOf("Information Technology", "Education", "Banking", "Tourism and Tour Guiding")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, faculties)
        binding.faculty.setAdapter(adapter)

        binding.submitBtn.setOnClickListener {
            val email = arguments?.getString("name").toString()
            val password = arguments?.getString("email").toString()
            val fullname = binding.name.text.toString().trim()
            val studentid = binding.password.text.toString().trim()
            val selectedFaculty = binding.faculty.text.toString()
            val faculty = faculties.indexOf(selectedFaculty) + 1
            val phone = binding.phone.text.toString().trim()

            if (fullname.isNotEmpty() && studentid.isNotEmpty() && faculty!= -1 && phone.isNotEmpty()) {
                if (binding.checkbox.isChecked == false) {
                    Toast.makeText(context, "Please check the box", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.registerUser(email, password, phone, fullname, studentid, faculty, object : RegistrationViewModel.AuthCallback {
                        override fun onSuccess() {
                            Toast.makeText(requireContext(), "Register success", Toast.LENGTH_SHORT).show()
                            activity?.finish()
                        }
                        override fun onFailure(message: String) {
                            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            } else {
                Toast.makeText(requireContext(), "Please enter enough information", Toast.LENGTH_SHORT).show()            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}