package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.ProjectDTO
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.Query

interface ProjectService {
    @GET("api/projects")
    fun getAllProjects(@Query("username") username: String?,): Call<ResponseWrapper<List<ProjectResponse>>>

    @POST("api/projects")
    suspend fun createProject(@Body project: ProjectDTO): Response<ProjectDTO>
}