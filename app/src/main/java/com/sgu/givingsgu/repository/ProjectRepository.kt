package com.sgu.givingsgu.repository

import retrofit2.Response
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.model.ProjectDTO
import com.sgu.givingsgu.model.User
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.ProjectService
import com.sgu.givingsgu.network.response.ProjectResponse
import com.sgu.givingsgu.network.response.ResponseWrapper
import com.sgu.givingsgu.utils.DataLocalManager
import retrofit2.Call

class ProjectRepository {
    private val projectApiService = RetrofitClient.createService(ProjectService::class.java)

    fun getAllProjects(): Call<ResponseWrapper<List<ProjectResponse>>> {
        if (DataLocalManager.isLoggedIn()) {
            return projectApiService.getAllProjects(DataLocalManager.getUser()?.userId.toString())
        }
        return projectApiService.getAllProjects(null)
    }

    suspend fun createProject(project: ProjectDTO): Response<ProjectDTO> {
        return projectApiService.createProject(project)
    }


    fun likeProject(projectId: Long): Call<ResponseWrapper<List<String>>> {
        return projectApiService.likeProject(projectId, DataLocalManager.getUser()?.userId!!)
    }

    fun unlikeProject(projectId: Long): Call<ResponseWrapper<List<String>>> {
        return projectApiService.unlikeProject(projectId, DataLocalManager.getUser()?.userId!!)
    }


    suspend fun updateProject(id: Long, project: ProjectDTO): Response<ProjectDTO> {
        return projectApiService.updateProject(id, project)
    }
}