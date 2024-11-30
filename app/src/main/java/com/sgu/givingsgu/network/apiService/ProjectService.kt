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

import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface ProjectService {
    @GET("api/projects")
    fun getAllProjects(@Query("username") username: String?,): Call<ResponseWrapper<List<ProjectResponse>>>

    @POST("api/projects")
    suspend fun createProject(@Body project: ProjectDTO): Response<ProjectDTO>


    @GET("api/project-likes/{projectId}")
    fun likeProject(@Path("projectId") projectId: Long,@Query("userId") userId: Long,): Call<ResponseWrapper<List<String>>>


    @DELETE("api/project-likes/{projectId}")
    fun unlikeProject(@Path("projectId") projectId: Long,@Query("userId") userId: Long,): Call<ResponseWrapper<List<String>>>

  
    @PATCH("api/projects/{id}")
    suspend fun updateProject(@Path("id") id: Long, @Body project: ProjectDTO): Response<ProjectDTO>

}