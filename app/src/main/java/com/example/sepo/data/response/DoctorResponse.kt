package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class DoctorResponse(

	@field:SerializedName("DoctorResponse")
	val doctorResponse: List<DoctorResponseItem?>? = null
)

data class DoctorResponseItem(

	@field:SerializedName("special")
	val special: String? = null,

	@field:SerializedName("address_hospital")
	val addressHospital: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("Time")
	val time: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("number_phone")
	val numberPhone: String? = null
)
