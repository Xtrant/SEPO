package com.example.sepo.data.response

import com.google.gson.annotations.SerializedName

data class VideoResponseItem(

	@field:SerializedName("duration")
	val duration: String? = null,

	@field:SerializedName("video_url")
	val videoUrl: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("category")
	val category: String? = null,

	@field:SerializedName("thumbnail_url")
	val thumbnailUrl: String? = null,

	var isWatched: Boolean = false
)
