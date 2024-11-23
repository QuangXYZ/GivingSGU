package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Comment
import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.UserReward
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.CommentService
import com.sgu.givingsgu.network.apiService.RewardService
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.utils.DataLocalManager
import retrofit2.Call

class RewardRepository {
    private val rewardApiService = RetrofitClient.createService(RewardService::class.java)

    fun getAllReward(): Call<ResponseWrapper<List<Reward>>> {
        return rewardApiService.getAllReward()
    }

    fun redeem(userId: String, rewardId: String): Call<ResponseWrapper<UserReward>> {
        return rewardApiService.redeem(userId, rewardId)
    }

    fun getAllRewardHistory(userId: Long): Call<ResponseWrapper<List<UserReward>>> {
        return rewardApiService.getAllUserReward(userId)
    }
}