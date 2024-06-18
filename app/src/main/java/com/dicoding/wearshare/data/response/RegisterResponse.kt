package com.dicoding.wearshare.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("user")
    val user: UserResponse,

    @SerializedName("message")
    val message: String
)

data class UserResponse(
    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
