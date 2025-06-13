package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ScoreResponse(

	@field:SerializedName("behave_score")
	val behaveScore: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("knowledge_score")
	val knowledgeScore: String? = null,

	@field:SerializedName("profile_id")
	val profileId: String? = null,

	@field:SerializedName("hrq_score")
	val hrqScore: String? = null,

	@field:SerializedName("osteoporosis_score")
	val osteoporosisScore: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("osteoarthritis_score")
	val osteoarthritisScore: String? = null
)
