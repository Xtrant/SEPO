package com.example.sepo.ui.adapter

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sepo.data.response.VideoResponseItem
import com.example.sepo.databinding.ItemVideoBinding
import android.webkit.WebChromeClient
import android.webkit.WebViewClient

class VideoAdapter(
    private val list: List<VideoResponseItem>,
    private val activity: Activity,
    private val fullscreenContainer: FrameLayout
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isWebViewVisible = false

        fun bind(video: VideoResponseItem) {
            Glide.with(binding.root.context)
                .load(video.thumbnailUrl)
                .into(binding.ivThumbnail)
            binding.tvName.text = video.title
            binding.tvDuration.text = video.duration
            binding.tvCategory.text = video.category
            binding.tvView.text = if (video.isWatched) "Sudah ditonton" else "Belum ditonton"

            binding.webView.settings.javaScriptEnabled = true
            binding.webView.settings.domStorageEnabled = true
            binding.webView.settings.mediaPlaybackRequiresUserGesture = false
            binding.webView.webViewClient = WebViewClient()

            binding.webView.webChromeClient = object : WebChromeClient() {
                private var customView: View? = null
                private var customViewCallback: CustomViewCallback? = null

                override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                    if (customView != null) {
                        onHideCustomView()
                        return
                    }
                    customView = view
                    customViewCallback = callback

                    fullscreenContainer.visibility = View.VISIBLE
                    fullscreenContainer.addView(
                        view,
                        FrameLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    )

                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    binding.root.visibility = View.GONE
                    activity.window.decorView.systemUiVisibility = (
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            )
                }

                override fun onHideCustomView() {
                    fullscreenContainer.visibility = View.GONE
                    customView?.let { fullscreenContainer.removeView(it) }
                    customView = null
                    binding.root.visibility = View.VISIBLE
                    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    customViewCallback?.onCustomViewHidden()
                    activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE

                }
            }

            binding.webView.visibility = if (isWebViewVisible) View.VISIBLE else View.GONE
            if (isWebViewVisible) {
                val html = "<iframe width=\"100%\" height=\"100%\" src=\"${video.videoUrl}\" frameborder=\"0\" allowfullscreen></iframe>"
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
