package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class ProfileResponseItem(
    @SerializedName("profile_id")
    val profileId: String,

    @SerializedName("user_id")
    val userId: String,

    @SerializedName("profile_name")
    val profileName: String,

    @SerializedName("age")
    val age: String,

    @SerializedName("gender")
    val gender: String,

    @SerializedName("created_at")
    val createdAt: String
)
