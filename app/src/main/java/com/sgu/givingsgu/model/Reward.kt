package com.sgu.givingsgu.model

data class Reward(
    val id: Long,

    val name: String,

    val pointsRequired: Int,

    val quantity: Int,

    val imageUrl: String,

    val description: String
)