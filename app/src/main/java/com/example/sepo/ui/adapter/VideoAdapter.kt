package com.example.sepo.ui.adapter

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sepo.R
import com.example.sepo.data.response.VideoResponseItem
import com.example.sepo.databinding.ItemVideoBinding
import com.example.sepo.ui.test.TestViewModel
import com.example.sepo.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth

class VideoAdapter(
    private val list: List<VideoResponseItem>,
    private val activity: Activity,
    private val fullscreenContainer: FrameLayout,
    private val viewModel: TestViewModel,
    private val sessionManager: SessionManager
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    inner class VideoViewHolder(val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isWebViewVisible = false
        private var customView: View? = null
        private var customViewCallback: WebChromeClient.CustomViewCallback? = null

        fun bind(video: VideoResponseItem) {
            Glide.with(binding.root.context)
                .load(R.drawable.thumbnail_umum)
                .into(binding.ivThumbnail)

            binding.tvName.text = video.title
            binding.tvDuration.text = video.duration
            binding.tvCategory.text = video.category

            val isWatched = sessionManager.getWatchedVideos().contains(video.id.toString())
            video.isWatched = isWatched
            binding.tvView.text = if (isWatched) "Sudah ditonton" else "Belum ditonton"

            setupWebView(video.videoUrl.toString())

            binding.webView.visibility = if (isWebViewVisible) View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                isWebViewVisible = !isWebViewVisible
                video.isWatched = true
                sessionManager.addWatchedVideo(video.id.toString())
                notifyItemChanged(bindingAdapterPosition)

                if (sessionManager.isAllVideosWatched(list.map { it.id.toString() })) {
                    Toast.makeText(activity, "Semua video sudah ditonton!", Toast.LENGTH_SHORT).show()
                    val uid = auth.currentUser?.uid
                    val profileId = sessionManager.getProfileId()
                    viewModel.getSaveProfileStatus(uid ?: "", profileId, null, null, 1)
                }
            }
        }

        private fun setupWebView(videoUrl: String) {
            binding.webView.apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.mediaPlaybackRequiresUserGesture = false
                webViewClient = WebViewClient()

                webChromeClient = object : WebChromeClient() {
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
                        customViewCallback?.onCustomViewHidden()
                        customViewCallback = null

                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        binding.root.visibility = View.VISIBLE
                        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
                    }
                }

                val html = """
                    <html><body style="margin:0;">
                        <iframe width="100%" height="100%" src="$videoUrl"
                        frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
                        allowfullscreen></iframe>
                    </body></html>
                """.trimIndent()

                loadDataWithBaseURL(null, html, "text/html", "utf-8", null)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}
