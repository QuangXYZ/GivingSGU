package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Donation
import com.sgu.givingsgu.network.response.DonationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DonationService {
    @GET("api/donations/top10")
    suspend fun getTop10Donations(): List<DonationResponse>
    @GET("api/donations/top10/{projectId}")
    suspend fun getTop10DonationsByProjectId(@Path("projectId") projectId: Long): List<DonationResponse>
}