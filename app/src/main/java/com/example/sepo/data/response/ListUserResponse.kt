package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ListUserResponse(

	@field:SerializedName("ListUserResponse")
	val listUserResponse: List<ListUserResponseItem?>? = null
)

data class ListUserResponseItem(

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("email")
	val email: String? = null
)

data class Question(
	val id: String = "",
	val question: String = "",
	val options: List<AnswerOption> = listOf(),
	val correctAnswer: String? = null, // Hanya untuk post-test
	val points: Int? = null // Hanya untuk pre-test
)

data class UserAnswer(
	val username: String,
	val question: String,
	val selectedOption: String,
	val points : Int? = null
)

data class AnswerOption(
	val text: String = "",
	val points: Int = 0
)

data class Message(
	val text: String? = null,
	val name: String? = null,
	val photoUrl: String? = null,
	val timestamp: Long? = null,
	val uid: String? = ""
)

