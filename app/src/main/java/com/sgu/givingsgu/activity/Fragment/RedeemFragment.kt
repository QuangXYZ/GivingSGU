package com.sgu.givingsgu.activity.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sgu.givingsgu.activity.RewardActivity
import com.sgu.givingsgu.activity.RewardHistoryActivity
import com.sgu.givingsgu.databinding.FragmentRedeemBinding
import com.sgu.givingsgu.utils.DataLocalManager
import java.text.NumberFormat
import java.util.Locale

class RedeemFragment : Fragment() {
    private lateinit var binding: FragmentRedeemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRedeemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }
    private fun init() {
        settingUpListener()

        if (DataLocalManager.getUser()?.points != null){
            binding.textView15.text = formatCurrency(DataLocalManager.getUser()?.points!!)

        }
        else {
            binding.textView15.text = "0 RP"
        }

    }
    private fun settingUpListener() {
        binding.reward.setOnClickListener {
            val intent = Intent(requireContext(), RewardActivity::class.java)

            startActivity(intent)

        }
        binding.history.setOnClickListener {
            val intent = Intent(requireContext(), RewardHistoryActivity::class.java)
            startActivity(intent)
        }

    }

    fun formatCurrency(amount: Int): String {
        val formatted = NumberFormat.getNumberInstance(Locale.US).format(amount)
        return "$formatted RP"
    }
}