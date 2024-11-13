package com.sgu.givingsgu.network.request

data class TransactionRequest(
    val transactionId: String,
    val projectId: Long,
    val userId: Long,
    val amount: Double,
    val paymentMethod: String,
    val token: String
    )