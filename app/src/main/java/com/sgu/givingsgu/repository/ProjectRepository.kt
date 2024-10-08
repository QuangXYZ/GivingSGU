package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.network.RetrofitClient

class ProjectRepository {
    private val apiService = RetrofitClient.apiService

    suspend fun getAllProjects(): List<Project> {
        return apiService.getAllProjects()
    }
}