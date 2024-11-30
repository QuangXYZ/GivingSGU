package com.sgu.givingsgu.network.response

import java.util.Date

class DonationResponse(
    val userId: Long,
    val fullName: String,
    val imageUrl: String,
    val totalAmount: Double,
    val donateDate: Date
)
