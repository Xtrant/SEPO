package com.example.sepo.ui.test

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.data.response.PreTestResponseItem
import com.example.sepo.data.response.UserAnswerPreTest
import com.example.sepo.databinding.ActivityPreTestBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.PreTestAdapter
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PreTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreTestBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: PreTestAdapter
    private val userAnswers = mutableListOf<UserAnswerPreTest>() // Menyimpan jawaban user
    private var behaveTotalScore = 0
    private var hrqTotalScore = 0
    private val viewModel: TestViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeViewModel()

        viewModel.getPreTest()

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
//        loadQuestionsFromFirestore()
//
        binding.btnSbmt.setOnClickListener {
            calculateScore()
        }
    }

    private fun observeViewModel() {
        viewModel.preTestResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    binding.btnSbmt.visibility = View.VISIBLE
                    adapter.submitList(result.data)
                }

                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    binding.btnSbmt.visibility = View.VISIBLE
                    Toast.makeText(this, "Gagal membuat profil: ${result.error}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = PreTestAdapter { question, answerText, behavePoints, hrqPoints ->
            saveUserAnswer(question, answerText, behavePoints, hrqPoints)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PreTestActivity)
            adapter = this@PreTestActivity.adapter
        }
    }

    private fun saveUserAnswer(question: PreTestResponseItem, selectedOption: String, behavePoints: Int, hrqPoints: Int) {
        val session = SessionManager(this)
        val profileName = session.getProfileName()
        val userAnswer = UserAnswerPreTest(
            profileName.toString(),
            question.question.toString(),
            selectedOption,
            behavePoints,
            hrqPoints
        )

        val existingIndex = userAnswers.indexOfFirst { it.question == question.question }
        if (existingIndex != -1) {
            userAnswers[existingIndex] = userAnswer // Update jawaban jika sudah ada
        } else {
            userAnswers.add(userAnswer) // Tambah jawaban baru
        }
    }

    private fun calculateScore() {
        behaveTotalScore = userAnswers.sumOf { it.behavePoints }
        hrqTotalScore = userAnswers.sumOf { it.hrqPoints }
        val session = SessionManager(this)
        val userId = auth.currentUser?.uid
        val profileId = session.getProfileId()
        val scoreMessage = "Total Skor Pre-Test: \n\n Skor Behaviour (Perilaku) : $behaveTotalScore \n Skor HRQoL : $hrqTotalScore"
        userAnswers.forEach { userAnswer ->
            val question = adapter.currentList.find { it.question == userAnswer.question }
            question?.let {
                viewModel.getSaveAnswerPreTest(
                    userId.toString(),
                    profileId,
                    it.id,
                    userAnswer.selectedOption
                )

            }
        }

        viewModel.getSaveScore(
            userId.toString(),
            profileId,
            0,
            behaveTotalScore,
            hrqTotalScore,
            0,
            0
        )

        showResultDialog(scoreMessage)
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Hasil Pre-Test")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
}



