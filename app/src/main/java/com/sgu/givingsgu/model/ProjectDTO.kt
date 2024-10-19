package com.sgu.givingsgu.model

// DTO để post lên server, không thay đổi Project
data class ProjectDTO(
    val projectId: Long = 0,
    val name: String,
    val description: String? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val targetAmount: Double,
    val currentAmount: Double? = null,
    val status: String? = null,
    val numberDonors: Int = 0,
    val imageUrls: String? = null,
    val managedBy: Long
)

