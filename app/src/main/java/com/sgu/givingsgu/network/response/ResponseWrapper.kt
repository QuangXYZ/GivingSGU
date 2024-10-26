package com.sgu.givingsgu.network.response

data class ResponseWrapper<T>(
    val status: Int,
    val message: String,
    val data: T?
)