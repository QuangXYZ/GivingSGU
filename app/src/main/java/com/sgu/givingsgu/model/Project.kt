package com.sgu.givingsgu.model


import java.util.Date

data class Project(
    val projectId: Long = 0,
    val name: String,
    val description: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val targetAmount: Double,
    val currentAmount: Double? = null,
    val status: String? = null,
    val numberDonors: Int = 0,
    val imageUrls: String? = null,
    val managedBy: Long
)