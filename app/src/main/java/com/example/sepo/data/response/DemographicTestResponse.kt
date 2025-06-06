package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class DemographicTestResponseItem(

	@field:SerializedName("question")
	val question: String,

	@field:SerializedName("options")
	val options: List<DemoOptionsItem>,

	@field:SerializedName("id")
	val id: Int,

	var isError: Boolean = false
)

data class DemoOptionsItem(

	@field:SerializedName("text")
	val text: String ? = null,

	@field:SerializedName("points_arthritis")
	val pointsArthritis: Int,

	@field:SerializedName("points_osteoporosis")
	val pointsOsteoporosis: Int
)
