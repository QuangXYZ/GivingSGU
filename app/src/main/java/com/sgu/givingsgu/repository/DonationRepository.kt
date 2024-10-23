package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Donation
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.CommentService
import com.sgu.givingsgu.network.apiService.DonationService
import com.sgu.givingsgu.network.response.CommentResponse

class DonationRepository {
    private val donationApiService = RetrofitClient.createService(DonationService::class.java)
    suspend fun getDonationsByProjectId(projectId: Long): List<Donation> {
        return donationApiService.getDonationsByProjectId(projectId)
    }


}