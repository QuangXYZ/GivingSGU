package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Page
import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.network.response.TransactionResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface TransactionService {
    @POST("api/payment/process")
    fun submitTransaction(@Body transaction: TransactionRequest): Call<ResponseWrapper<Transaction>>

    @GET("api/transactions/project/{projectId}")
    fun getTransactionsByProjectId(
        @Path("projectId") projectId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<ResponseWrapper<Page<TransactionResponse>>>

    @GET("api/transactions/export/excel/{projectId}")
    fun exportTransactionsToExcel(
        @Path("projectId") projectId: Long
    ): Call<ResponseBody>

}