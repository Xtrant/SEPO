package com.example.sepo.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sepo.R
import com.example.sepo.ui.onboarding.OnboardingFragment

//TODO ubah title, gambar, dan onboarding di sini

class OnboardingAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val onboardingData = listOf(
        OnboardingData(R.drawable.baseline_person_24, "Title 1", "Description 1"),
        OnboardingData(R.drawable.baseline_visibility_24, "Title 2", "Description 2"),
        OnboardingData(R.drawable.baseline_visibility_off_24, "Title 3", "Description 3")
    )

    override fun getItemCount(): Int = onboardingData.size

    override fun createFragment(position: Int): Fragment {
        val data = onboardingData[position]
        return OnboardingFragment.newInstance(data.imageRes, data.title, data.description, position)
    }
}

data class OnboardingData(val imageRes: Int, val title: String, val description: String)

