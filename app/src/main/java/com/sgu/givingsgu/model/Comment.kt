package com.sgu.givingsgu.model

data class Comment(
    val commentId: Long?,
    val userId: Long,
    val projectId: Long,
    val content: String
)