package com.sgu.givingsgu.model

import java.util.Date

data class Transaction(
    val id: Long? = null,
    val donationId: Long,
    val amount: Double,
    val transactionDate: Date,
    val paymentMethod: String,
    val status: String,
    val token: String,
    val transactionId: String
)