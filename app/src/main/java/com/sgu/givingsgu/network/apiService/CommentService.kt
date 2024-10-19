package com.sgu.givingsgu.network.apiService

import com.sgu.givingsgu.model.Comment
import com.sgu.givingsgu.model.Project
import com.sgu.givingsgu.network.response.CommentResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CommentService {
    @GET("api/comments/project/{projectId}")
    suspend fun getCommentsByProjectId(@Path("projectId") projectId: Long): List<CommentResponse>
}