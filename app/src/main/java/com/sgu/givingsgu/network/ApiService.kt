package com.sgu.givingsgu.network

import com.sgu.givingsgu.model.Project
import retrofit2.http.GET

interface ApiService {

    @GET("api/projects")
    suspend fun getAllProjects(): List<Project>
}