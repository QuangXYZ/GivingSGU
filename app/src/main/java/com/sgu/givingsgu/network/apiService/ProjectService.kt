package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.ProjectDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response

interface ProjectService {
    @GET("api/projects")
    suspend fun getAllProjects(): List<Project>

    @POST("api/projects")
    suspend fun createProject(@Body project: ProjectDTO): Response<ProjectDTO>
}