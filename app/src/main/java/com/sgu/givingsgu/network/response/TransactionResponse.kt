package com.sgu.givingsgu.network.response

data class TransactionResponse(
    val id: Long,
    val amount: Double,
    val transactionDate: String,
    val paymentMethod: String,
    val status: String,
    val token: String?,
    val transactionId: String?,
    val fullName: String,
    val facultyName: String,
    val studentId: String
)
