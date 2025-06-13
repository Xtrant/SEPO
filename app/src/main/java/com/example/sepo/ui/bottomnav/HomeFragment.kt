package com.example.sepo.ui.bottomnav

import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.TypefaceSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.databinding.FragmentHomeBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.RecommendAdapter
import com.example.sepo.ui.education.EducationActivity
import com.example.sepo.ui.list.ListUserViewModel
import com.example.sepo.ui.test.PostTestActivity
import com.example.sepo.ui.test.PreTestActivity
import com.example.sepo.ui.test.TestViewModel
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private var isPostTest = 0
    private var isPreTest = 0
    private var isEducation = 0
    private val viewModel: ListUserViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    private val viewModelStatus: TestViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
    }

    override fun onResume() {
        super.onResume()
        val uId = auth.currentUser?.uid

        val session = SessionManager(requireContext())
        val profileId = session.getProfileId()
        viewModelStatus.getProfileStatus(uId.toString(), profileId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSpannableTextButtonTest()

        setSpannableTextButtonLatihan()

        setBackgroundCardView()

        setTitleGreeting()

        binding?.icPostTest?.setOnClickListener {
            if (isPreTest == 0 || isEducation == 0 ) {
                showDialog(
                    "Pre-Test dan Edukasi Wajib Diselesaikan",
                    "Untuk melanjutkan ke materi Post Test, Anda diwajibkan menyelesaikan Pre-Test dan Menonton Semua Video Edukasi terlebih dahulu."
                )
            } else {
                startActivity(Intent(requireContext(), PostTestActivity::class.java))
            }
        }

        binding?.icPreTest?.setOnClickListener {
            startActivity(Intent(requireContext(), PreTestActivity::class.java))
        }
        binding?.icEdukasi?.setOnClickListener {
            if (isPreTest == 0) {
                showDialog(
                    "Pre-Test Wajib Diselesaikan",
                    "Untuk melanjutkan ke materi Edukasi, Anda diwajibkan menyelesaikan Pre-Test terlebih dahulu."
                )

            } else {
                startActivity(Intent(requireContext(), EducationActivity::class.java))
            }
        }

        binding?.viewAll?.setOnClickListener {
            if (isPreTest == 0) {
                showDialog(
                    "Pre-Test Wajib Diselesaikan",
                    "Untuk melanjutkan ke materi Edukasi, Anda diwajibkan menyelesaikan Pre-Test terlebih dahulu."
                )
            } else {

                startActivity(Intent(requireContext(), EducationActivity::class.java))
            }


        }

        val uId = auth.currentUser?.uid

        val session = SessionManager(requireContext())
        val profileId = session.getProfileId()

        viewModel.listEdukasi()

        viewModelStatus.getProfileStatus(uId.toString(), profileId)

        observeViewModel()

    }

    private fun observeViewModel() {
        viewModel.resultListEdukasi.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding?.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding?.progressBar)
                    val adapter = RecommendAdapter(result.data, requireContext()) { video ->
                        if (isPreTest == 0) {
                            showDialog(
                                "Pre-Test Wajib Diselesaikan",
                                "Untuk melanjutkan ke materi Edukasi, Anda diwajibkan menyelesaikan Pre-Test terlebih dahulu."
                            )
                        } else {

                            startActivity(Intent(requireContext(), EducationActivity::class.java))
                        }

                    }
                    binding?.rvMateri?.layoutManager = LinearLayoutManager(context)
                    binding?.rvMateri?.adapter = adapter
                }


                is Result.Error -> {
                    showLoading(false, binding?.progressBar)

                }
            }
        }

        viewModelStatus.profileStatus.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding?.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding?.progressBar)
                    isPreTest = result.data.isPretest
                    isPostTest = result.data.isPostTest
                    isEducation = result.data.isCompletedEducation

                    if (isPreTest == 1) {
                        binding?.icEdukasi?.setImageResource(R.drawable.ic_education_on)
                    }

                    if (isPreTest == 1 && isEducation == 1) {
                        binding?.icPostTest?.setImageResource(R.drawable.ic_post_test_on)
                    }
                }


                is Result.Error -> {
                    showLoading(false, binding?.progressBar)

                }
            }
        }
    }

    private fun setSpannableTextButtonTest() {

        val spannableTest = SpannableString("Rekap test\n\nLihat detail")

        val semiBoldFont = ResourcesCompat.getFont(requireContext(), R.font.inter_tight_semi_bold)

        val regularFont = ResourcesCompat.getFont(requireContext(), R.font.inter_tight_regular)

        // Atur gaya teks untuk Baris Pertama
        spannableTest.setSpan(AbsoluteSizeSpan(16, true), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            spannableTest.setSpan(
                TypefaceSpan(semiBoldFont ?: Typeface.DEFAULT),
                0,
                11,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableTest.setSpan(
                TypefaceSpan(regularFont ?: Typeface.DEFAULT),
                12,
                spannableTest.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Atur gaya teks untuk Baris Kedua
        spannableTest.setSpan(
            AbsoluteSizeSpan(14, true),
            12,
            spannableTest.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.btnTest?.text = spannableTest
    }

    private fun setSpannableTextButtonLatihan() {

        val spannableLatihan = SpannableString("Rekap Latihan\n\nLihat detail")

        val semiBoldFont = ResourcesCompat.getFont(requireContext(), R.font.inter_tight_semi_bold)

        val regularFont = ResourcesCompat.getFont(requireContext(), R.font.inter_tight_regular)

        // Atur gaya teks untuk Baris Pertama
        spannableLatihan.setSpan(
            AbsoluteSizeSpan(16, true),
            0,
            14,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            spannableLatihan.setSpan(
                TypefaceSpan(semiBoldFont ?: Typeface.DEFAULT),
                0,
                14,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableLatihan.setSpan(
                TypefaceSpan(regularFont ?: Typeface.DEFAULT),
                15,
                spannableLatihan.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }

        // Atur gaya teks untuk Baris Kedua
        spannableLatihan.setSpan(
            AbsoluteSizeSpan(14, true),
            15,
            spannableLatihan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.btnLatihan?.text = spannableLatihan
    }

    private fun showDialog(title: String, message: String) {

        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Mengerti") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun setBackgroundCardView() {
        binding?.cvHome?.setBackgroundResource(R.drawable.rounded_card)
    }

    private fun setTitleGreeting() {
        val session = SessionManager(requireContext())
        val profileName = session.getProfileName()
        val kondisi = session.getCondition()

        binding?.tvTitleGreeting?.text =
            getString(R.string.title_greeting_user, profileName) + "\n\n Kamu $kondisi "
    }
}


