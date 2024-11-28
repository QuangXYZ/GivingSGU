package com.sgu.givingsgu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.UserReward
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.repository.ProjectRepository
import com.sgu.givingsgu.repository.RewardRepository
import com.sgu.givingsgu.utils.DataLocalManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RewardViewModel : ViewModel() {
    private val rewardRepository = RewardRepository()

    private val _reward = MutableLiveData<List<Reward>>()
    val reward: LiveData<List<Reward>> get() = _reward

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchAllReward() {
        viewModelScope.launch {
            rewardRepository.getAllReward().enqueue(object :
                Callback<ResponseWrapper<List<Reward>>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<List<Reward>>>,
                    response: Response<ResponseWrapper<List<Reward>>>
                ) {
                    if (response.isSuccessful) {
                        _reward.value = response.body()?.data!!
                    } else {
                        _error.value = response.message()
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<List<Reward>>>, t: Throwable) {
                    _error.value = t.message

                }
            })
        }
    }

    fun redeem(userId: String, rewardId: String, listener: RewardListener) {
        viewModelScope.launch {
            rewardRepository.redeem(userId, rewardId).enqueue(object :
                Callback<ResponseWrapper<UserReward>> {
                override fun onResponse(
                    call: Call<ResponseWrapper<UserReward>>,
                    response: Response<ResponseWrapper<UserReward>>
                ) {
                    if (response.isSuccessful) {
                        listener.onRewardSuccess(response.body()?.data!!)
                        DataLocalManager.getUser()?.let { user ->
                            user.points -= response.body()?.data?.reward?.pointsRequired ?: 0
                            DataLocalManager.saveUser(user)
                        }

                    } else {
                        listener.onRewardFailed(response.message())
                        _error.value = response.message()
                    }
                }

                override fun onFailure(call: Call<ResponseWrapper<UserReward>>, t: Throwable) {
                    _error.value = t.message

                }
            })
        }
    }

    interface RewardListener {
        fun onRewardSuccess(userReward: UserReward)
        fun onRewardFailed(message: String)
    }
}
