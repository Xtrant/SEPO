package com.example.sepo.ui.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sepo.R
import com.example.sepo.ui.onboarding.OnboardingFragment



class OnboardingAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val onboardingData = listOf(
        OnboardingData(R.drawable.onboarding_img, "Selamat Datang di SEPO", "SEPO membantumu mengenali risiko osteoporosis dan osteoarthritis melalui kuisioner dan edukasi interaktif. Semua bisa kamu lakukan secara mandiri dan mudah."),
        OnboardingData(R.drawable.onboarding_img, "Isi Kuisioner & Dapatkan Hasilmu", "Jawab beberapa pertanyaan tentang kondisi tubuh dan kebiasaan harianmu. Aplikasi akan menunjukkan apakah kamu berisiko terkena osteoporosis atau osteoarthritis."),
        OnboardingData(R.drawable.onboarding_img, "Edukasi dan Konsultasi Langsung", "Dapatkan tips, latihan, dan edukasi sesuai hasil tesmu. Butuh bantuan lebih lanjut? Gunakan fitur live chat untuk konsultasi langsung."
                )
    )

    override fun getItemCount(): Int = onboardingData.size

    override fun createFragment(position: Int): Fragment {
        val data = onboardingData[position]
        return OnboardingFragment.newInstance(data.imageRes, data.title, data.description, position)
    }
}

data class OnboardingData(val imageRes: Int, val title: String, val description: String)

