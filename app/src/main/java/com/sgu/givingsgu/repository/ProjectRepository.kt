package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.CommentService
import com.sgu.givingsgu.network.apiService.ProjectService

class ProjectRepository {
    private val projectApiService = RetrofitClient.createService(ProjectService::class.java)

    suspend fun getAllProjects(): List<Project> {
        return projectApiService.getAllProjects()
    }
}