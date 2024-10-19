package com.sgu.givingsgu.repository

import retrofit2.Response
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.ProjectDTO
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.ProjectService

class ProjectRepository {
    private val projectApiService = RetrofitClient.createService(ProjectService::class.java)

    suspend fun getAllProjects(): List<Project> {
        return projectApiService.getAllProjects()
    }

    suspend fun createProject(project: ProjectDTO): Response<ProjectDTO> {
        return projectApiService.createProject(project)
    }
}