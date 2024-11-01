package com.sgu.givingsgu.network.response

import com.sgu.givingsgu.model.User

class LoginResponse(
    val user:User,
    val token:String
    )