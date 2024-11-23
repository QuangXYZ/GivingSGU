package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Reward
import com.sgu.givingsgu.model.UserReward
import com.sgu.givingsgu.network.response.DonationResponse
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RewardService {
    @GET("api/reward")
    fun getAllReward(): Call<ResponseWrapper<List<Reward>>>

    @POST("api/reward/redeem")
    fun redeem(
        @Query("userId") userId: String,
        @Query("rewardId") rewardId: String
    ): Call<ResponseWrapper<UserReward>>


    @GET("api/reward/history/{userId}")
    fun getAllUserReward(@Path("userId") userId: Long): Call<ResponseWrapper<List<UserReward>>>

}