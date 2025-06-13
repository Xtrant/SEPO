package com.example.sepo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sepo.R
import com.example.sepo.data.response.VideoResponseItem
import com.example.sepo.databinding.ItemVideoBinding
import com.example.sepo.utils.SessionManager

class RecommendAdapter(
    private val list: List<VideoResponseItem>,
    private val context: android.content.Context,
    private val onItemClick: (VideoResponseItem) -> Unit


    ) : RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>() {

    inner class RecommendViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val sessionManager = SessionManager(context)


        fun bind(video: VideoResponseItem) {
            // Tampilkan data
            Glide.with(binding.root.context)
                .load(R.drawable.thumbnail_umum)
                .into(binding.ivThumbnail)
            binding.tvName.text = video.title
            binding.tvDuration.text = video.duration
            binding.tvCategory.text = video.category
            video.isWatched = sessionManager.getWatchedVideos().contains(video.id.toString())
            binding.tvView.text = if (video.isWatched) "Sudah ditonton" else "Belum ditonton"

            // Toggle visibilitas saat diklik
            binding.root.setOnClickListener {
                onItemClick(video)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecommendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = 1
}