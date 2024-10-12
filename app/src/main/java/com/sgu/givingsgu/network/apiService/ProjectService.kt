package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Project
import retrofit2.http.GET

interface ProjectService {
    @GET("api/projects")
    suspend fun getAllProjects(): List<Project>
}