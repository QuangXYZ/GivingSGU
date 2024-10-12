package com.sgu.givingsgu.repository

import com.sgu.givingsgu.model.Comment
import com.sgu.givingsgu.network.RetrofitClient
import com.sgu.givingsgu.network.apiService.CommentService

class CommentRepository {
    private val commentApiService = RetrofitClient.createService(CommentService::class.java)

    suspend fun getCommentsByProjectId(projectId: Long): List<Comment> {
        return commentApiService.getCommentsByProjectId(projectId)
    }

}