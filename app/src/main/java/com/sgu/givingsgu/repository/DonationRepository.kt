package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Donation
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.CommentService
import com.sgu.givingsgu.network.apiService.DonationService
import com.sgu.givingsgu.network.response.CommentResponse
import com.sgu.givingsgu.network.response.DonationResponse

class DonationRepository {
    private val donationApiService = RetrofitClient.createService(DonationService::class.java)
    suspend fun getTop10Donations(): List<DonationResponse> {
        return donationApiService.getTop10Donations()
    }

    suspend fun getTop10DonationsByProjectId(projectId: Long): List<DonationResponse> {
        return donationApiService.getTop10DonationsByProjectId(projectId)
    }


}