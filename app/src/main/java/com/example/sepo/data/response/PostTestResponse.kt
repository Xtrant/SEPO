package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class PostTestResponse(

	@field:SerializedName("PostTestResponse")
	val postTestResponse: List<PostTestResponseItem?>? = null
)

data class PostTestResponseItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("options")
	val options: List<String?>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("correctAnswer")
	val correctAnswer: String? = null
)
