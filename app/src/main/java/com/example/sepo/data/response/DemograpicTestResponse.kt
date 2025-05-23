package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class DemograpicTestResponse(

	@field:SerializedName("DemograpicTestResponse")
	val demograpicTestResponse: List<DemograpicTestResponseItem>
)

data class DemograpicTestResponseItem(

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("options")
	val options: List<OptionsItem>,

	@field:SerializedName("id")
	val id: String
)

data class OptionsItem(

	@field:SerializedName("text")
	val text: String,

	@field:SerializedName("points_arthritis")
	val pointsArthritis: Int,

	@field:SerializedName("points_osteoporosis")
	val pointsOsteoporosis: Int
)
