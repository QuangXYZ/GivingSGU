package com.sgu.givingsgu.model

data class User(
    val userId: Long,
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val facultyId: Long,
    val fullName: String,
    val studentId: String,
    val role: String
)