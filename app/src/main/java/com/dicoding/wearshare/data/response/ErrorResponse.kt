package com.dicoding.wearshare.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @field:SerializedName("status")
    val status: Int? = null,

    @field:SerializedName("message")
    val message: String? = null
) {
    fun toErrorMessage(): String {
        return message ?: "Unknown error"
    }
}
