package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Page
import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.TransactionService
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.network.response.TransactionResponse
import okhttp3.ResponseBody
import retrofit2.Call

class TransactionRepository {
    private val transactionApiService = RetrofitClient.createService(TransactionService::class.java)
    fun submitTransaction(transactionRequest: TransactionRequest): Call<ResponseWrapper<Transaction>> {
        return transactionApiService.submitTransaction(transactionRequest)
    }
    fun getTransactionByProjectId(projectId: Long, page: Int, size: Int): Call<ResponseWrapper<Page<TransactionResponse>>> {
        return transactionApiService.getTransactionsByProjectId(projectId, page, size)
    }
    fun exportTransactionsToExcel(projectId: Long): Call<ResponseBody> {
        return transactionApiService.exportTransactionsToExcel(projectId)
    }



}