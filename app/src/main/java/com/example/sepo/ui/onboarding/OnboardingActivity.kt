package com.example.sepo.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.sepo.databinding.ActivityOnboardingBinding
import com.example.sepo.ui.adapter.OnboardingAdapter
import com.example.sepo.ui.authentication.LoginActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class OnboardingActivity : AppCompatActivity() {

    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager
        viewPager.adapter = OnboardingAdapter(this)

        val dotsIndicator: DotsIndicator = binding.dotsIndicator
        dotsIndicator.attachTo(viewPager)
    }

    fun goToNextPage() {
        val currentItem = viewPager.currentItem
        if (currentItem < (viewPager.adapter?.itemCount ?: 0) - 1) {
            // Pindah ke halaman berikutnya
            viewPager.setCurrentItem(currentItem + 1, true)
        } else {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}




