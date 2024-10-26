package com.sgu.givingsgu.repository

import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.PaymentService
import com.sgu.givingsgu.network.request.TransactionRequest
import okhttp3.ResponseBody
import retrofit2.Call

class TransactionRepository {
    private val transactionApiService = RetrofitClient.createService(PaymentService::class.java)
    suspend fun submitTransaction(transactionRequest: TransactionRequest): Call<ResponseBody> {
        return transactionApiService.submitTransaction(transactionRequest)
    }
}