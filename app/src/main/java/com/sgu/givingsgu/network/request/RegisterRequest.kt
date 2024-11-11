package com.sgu.givingsgu.network.request

data class RegisterRequest   (
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val facultyId: Long,
    val fullName: String,
    val studentId: String,
    val role: String,
    val imageUrl: String,
    val point: Int
)