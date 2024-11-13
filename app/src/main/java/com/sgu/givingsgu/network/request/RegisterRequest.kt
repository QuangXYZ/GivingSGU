package com.sgu.givingsgu.network.request

data class RegisterRequest   (
    val username: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val facultyId: Int,
    val fullName: String,
    val studentId: String,
    val role: String,
    val imageUrl: String,
    val points: Int
)