package com.dicoding.wearshare.data.response

import com.google.gson.annotations.SerializedName

data class DeteksiResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("imageUrl")
	val imageUrl: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Int? = null
)
