package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ProfileStatusResponse(

	@field:SerializedName("is_pretest")
	val isPretest: Int = 0,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("profile_id")
	val profileId: String? = null,

	@field:SerializedName("is_posttest")
	val isPostTest: Int = 0,

	@field:SerializedName("is_completed_education")
	val isCompletedEducation: Int = 0
)
