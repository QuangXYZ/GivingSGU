package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Donation
import retrofit2.http.GET
import retrofit2.http.Path

interface DonationService {
    @GET("api/comments/project/{projectId}")
    suspend fun getDonationsByProjectId(@Path("projectId") projectId: Long): List<Donation>
}