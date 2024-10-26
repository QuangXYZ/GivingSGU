package com.sgu.givingsgu.network

import android.content.Context
import com.sgu.givingsgu.utils.DataLocalManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = DataLocalManager.getToken()
        val requestBuilder = chain.request().newBuilder()

        // Nếu có token, thêm nó vào header
        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }

        return chain.proceed(requestBuilder.build())
    }
}