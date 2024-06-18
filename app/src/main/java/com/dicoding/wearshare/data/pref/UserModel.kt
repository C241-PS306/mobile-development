package com.dicoding.wearshare.data.pref

data class UserModel(
    val email: String,
    val password: String,
    val isLogin: Boolean = false
)