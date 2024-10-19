package com.sgu.givingsgu.network.response

data class CommentResponse(
    val commentId: Long,
    val projectId: Long,
    val content: String,
    val userId: Long,
    val username: String,
    val fullName: String,
    val imageUrl: String,
    val email: String
)
