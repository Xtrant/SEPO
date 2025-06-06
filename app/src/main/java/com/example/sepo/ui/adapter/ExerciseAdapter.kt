package com.example.sepo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sepo.R
import com.example.sepo.data.response.VideoResponseItem
import com.example.sepo.databinding.ItemVideoBinding

class ExerciseAdapter(
    private val list: List<VideoResponseItem>,
) : RecyclerView.Adapter<ExerciseAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isWebViewVisible = false

        fun bind(video: VideoResponseItem) {
            Glide.with(binding.root.context)
                .load(R.drawable.thumbnail_umum)
                .into(binding.ivThumbnail)
            binding.tvName.text = video.title
            binding.tvDuration.text = video.duration
            binding.tvCategory.text = video.category
            binding.tvView.text = if (video.isWatched) "Sudah ditonton" else "Belum ditonton"

            binding.webView.settings.javaScriptEnabled = true
            binding.webView.settings.domStorageEnabled = true
            binding.webView.settings.mediaPlaybackRequiresUserGesture = false
            binding.webView.webViewClient = WebViewClient()


            binding.webView.visibility = if (isWebViewVisible) View.VISIBLE else View.GONE
            if (isWebViewVisible) {
                val html = "<iframe width=\"100%\" height=\"100%\" src=\"${video.videoUrl}\" frameborder=\"0\"></iframe>"
                binding.webView.loadData(html, "text/html", "utf-8")
            }

            binding.root.setOnClickListener {
                isWebViewVisible = !isWebViewVisible
                video.isWatched = true
                notifyItemChanged(bindingAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}
