package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ConnectionResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
