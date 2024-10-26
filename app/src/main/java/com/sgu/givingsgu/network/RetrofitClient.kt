package com.sgu.givingsgu.network

import com.sgu.givingsgu.network.apiService.CommentService
import com.sgu.givingsgu.network.apiService.ProjectService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://givingsguserver-production.up.railway.app/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <Api> createService(apiClass: Class<Api>): Api {
        return retrofit.create(apiClass)
    }

    fun getClient(baseUrl: String): Retrofit {
        if (retrofit == null) {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
        }
        return retrofit!!
    }

}