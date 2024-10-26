package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.network.request.TransactionRequest
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface PaymentService {
    @POST("api/payment/process")
    suspend fun submitTransaction(@Body project: TransactionRequest): Call<ResponseBody>
}