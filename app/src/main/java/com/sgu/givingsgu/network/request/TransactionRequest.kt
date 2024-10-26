package com.sgu.givingsgu.network.request

data class TransactionRequest(
    val projectId: Long,
    val userId: Long,
    val amount: Double,
    val paymentMethod: String
    )