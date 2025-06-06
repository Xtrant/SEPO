package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponseItem(
    @SerializedName("profile_id")
    val profileId: Int,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("profile_name")
    val profileName: String,

    @SerializedName("age")
    val age: Int,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("kondisi")
    val kondisi: String,

    @SerializedName("dokter_id")
    val dokter_id: String,

    @SerializedName("created_at")
    val createdAt: String
)
