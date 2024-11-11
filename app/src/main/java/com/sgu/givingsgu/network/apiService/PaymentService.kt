package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Transaction
import com.sgu.givingsgu.network.request.TransactionRequest
import com.sgu.givingsgu.network.response.ResponseWrapper
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface PaymentService {
    @POST("api/payment/process")
    fun submitTransaction(@Body transaction: TransactionRequest): Call<ResponseWrapper<Transaction>>
}