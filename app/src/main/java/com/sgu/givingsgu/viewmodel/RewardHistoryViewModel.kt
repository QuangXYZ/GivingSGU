package com.sgu.givingsgu.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.UserReward
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.network.response.DonationResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.repository.CommentRepository
import com.sgu.givingsgu.repository.DonationRepository
import com.sgu.givingsgu.repository.RewardRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RewardHistoryViewModel : ViewModel() {
    private val rewardRepository = RewardRepository()
    private val _reward = MutableLiveData<List<UserReward>>()
    val reward: LiveData<List<UserReward>> get() = _reward

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error



    fun fetchAllUserReward(userId:Long) {
        viewModelScope.launch {
            rewardRepository.getAllRewardHistory(userId).enqueue(object :
                Callback<ResponseWrapper<List<UserReward>>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<List<UserReward>>>,
                    response: Response<ResponseWrapper<List<UserReward>>>
                ) {
                    if (response.isSuccessful) {
                        _reward.value = response.body()?.data!!
                    } else {
                        _error.value = response.message()
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<List<UserReward>>>, t: Throwable) {
                    _error.value = t.message

                }
            })

        }
    }


}