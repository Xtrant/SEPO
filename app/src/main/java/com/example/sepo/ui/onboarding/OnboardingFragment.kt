package com.example.sepo.ui.onboarding

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sepo.R
import com.example.sepo.databinding.FragmentOnboardingBinding


class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi binding
        _binding = FragmentOnboardingBinding.bind(view)

        // Akses elemen dengan binding
        val imageView = binding.imageView
        val titleText = binding.title
        val descriptionText = binding.description
        val btnNext = binding.btnNext

        // Mengambil data dari arguments atau set teks
        arguments?.let {
            imageView.setImageResource(it.getInt(ARG_IMAGE_RES))
            titleText.text = it.getString(ARG_TITLE)
            descriptionText.text = it.getString(ARG_DESCRIPTION)

            // Mendapatkan posisi fragment
            val position = it.getInt(ARG_POSITION)
            if (position == 2) { // Jika ini adalah halaman terakhir
                btnNext.text = getString(R.string.get_started)  // Ganti tombol menjadi "Get Started"
            } else {
                btnNext.text = getString(R.string.next)  // Ganti tombol menjadi "Next"
            }

            // Set listener untuk tombol Next
            btnNext.setOnClickListener {
                    val activity = requireActivity() as OnboardingActivity
                    activity.goToNextPage()
                }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_IMAGE_RES = "image_res"
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_POSITION = "position"

        fun newInstance(imageRes: Int, title: String, description: String, position: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE_RES, imageRes)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            args.putInt(ARG_POSITION, position)
            fragment.arguments = args
            return fragment
        }
    }
}





