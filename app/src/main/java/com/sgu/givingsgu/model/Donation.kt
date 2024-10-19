package com.sgu.givingsgu.model

import java.util.Date

data class Donation(
    val donationId: Long,
    val projectId: Long,
    val userId: Long,
    val amount: Double,
    val donateDate: Date,
    val status: String
)