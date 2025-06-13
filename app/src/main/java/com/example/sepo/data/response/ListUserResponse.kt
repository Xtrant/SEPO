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

data class UserAnswerDemographicTest(
	val profileName: String?,
	val question: String,
	val selectedOption: String,
	val osteoporosisPoints: Int,
	val osteoarthritisPoints : Int,
	val answerText : String,
)

data class Message(
	val fromUid: String? = null,
	val fromProfileId: String? = null,
	val fromProfileName: String? = null,
	val toDoctorId: String? = null,
	val toDoctorName: String? = null,
	val message: String? = null,
	val timestamp: Long? = null
)

data class MessageList(
	val userId: String? = null,
	val userName: String? = null,
	val doctorId: String? = null,
	val fromProfileId: String? = null,
	val message: String? = null,
	val timestamp: Long? = null
)


