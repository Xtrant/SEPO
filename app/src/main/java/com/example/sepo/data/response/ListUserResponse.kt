package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ListUserResponseItem(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)


data class UserAnswerPostTest(
	val profileName: String,
	val question: String,
	val selectedOption: String,
)

data class UserAnswerPreTest(
	val profileName: String,
	val question: String,
	val selectedOption: String,
	val behavePoints: Int,
	val hrqPoints : Int
)

data class Message(
	val text: String? = null,
	val name: String? = null,
	val timestamp: Long? = null,
	val uid: String? = ""
)

