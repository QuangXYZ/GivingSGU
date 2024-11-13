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
    val role: String,
    val points: Int
) {
    constructor(
        username: String,
        password: String,
        email: String,
        phoneNumber: String,
        facultyId: Long,
        fullName: String,
        studentId: String,
    ) : this(0, username, password, email, phoneNumber, facultyId, fullName, studentId, "USER", 100)

}