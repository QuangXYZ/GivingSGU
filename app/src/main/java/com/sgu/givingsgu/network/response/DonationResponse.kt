package com.sgu.givingsgu.network.response

class DonationResponse(
    val userId: Long,
    val fullName: String,
    val imageUrl: String,
    val totalAmount: Double
)
