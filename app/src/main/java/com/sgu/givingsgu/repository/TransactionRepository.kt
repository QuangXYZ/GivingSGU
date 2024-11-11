package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.PaymentService
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.ResponseWrapper
import okhttp3.ResponseBody
import retrofit2.Call

class TransactionRepository {
    private val transactionApiService = RetrofitClient.createService(PaymentService::class.java)
    fun submitTransaction(transactionRequest: TransactionRequest): Call<ResponseWrapper<Transaction>> {
        return transactionApiService.submitTransaction(transactionRequest)
    }
}