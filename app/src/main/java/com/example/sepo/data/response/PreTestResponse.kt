package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class PreTestResponse(

	@field:SerializedName("PreTestResponse")
	val preTestResponse: List<PreTestResponseItem?>? = null
)

data class PreTestResponseItem(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("options")
	val options: List<OptionsItem?>? = null,

	@field:SerializedName("id")
	val id: String? = null
)

data class OptionsItem(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("points")
	val points: Int? = null
)
