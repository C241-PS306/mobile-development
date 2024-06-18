package com.dicoding.wearshare.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("user")
    val user: User,

    @field:SerializedName("message")
    val message: String
)
data class User(
    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String
)
