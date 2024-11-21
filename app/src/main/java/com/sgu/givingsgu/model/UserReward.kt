package com.sgu.givingsgu.model

import java.util.Date

data class UserReward(
    val id: Long,
    val userId: Long,
    val reward: Reward,
    val redeemDate: Date,
    val status: String
)
