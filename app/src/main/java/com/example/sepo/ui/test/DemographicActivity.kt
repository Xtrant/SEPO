package com.example.sepo.ui.test

import android.content.Intent
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
import com.example.sepo.data.response.UserAnswerDemographicTest
import com.example.sepo.databinding.ActivityDemographicBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.DemographicAdapter
import com.example.sepo.ui.main.MainActivity
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
import com.google.firebase.auth.FirebaseAuth

class DemographicActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDemographicBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: DemographicAdapter
    private val userAnswers = mutableListOf<UserAnswerDemographicTest>() // Menyimpan jawaban user
    private var osteoporosisTotalScore = 0
    private var osteoarthritisTotalScore = 0
    private var condition = ""
    private val viewModel: TestViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDemographicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeViewModel()

        viewModel.getDemographicTest()

        auth = FirebaseAuth.getInstance()

        setupRecyclerView()

        binding.btnSbmt.setOnClickListener {
            submitAnswers()
        }
    }

    private fun observeViewModel() {
        viewModel.demographicTestResult.observe(this) { result ->
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
        adapter =
            DemographicAdapter { question, answerText, osteoporosisPoints, osteoarthritisPoints, answerEditText ->
                saveUserAnswer(question, answerText, osteoporosisPoints, osteoarthritisPoints, answerEditText)
            }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@DemographicActivity)
            adapter = this@DemographicActivity.adapter
        }
    }

    private fun saveUserAnswer(
        question: String,
        selectedOption: String,
        osteoporosisPoints: Int,
        osteoarthritisPoints: Int,
        answerEditText: String,
    ) {
        val session = SessionManager(this)
        val profileName = session.getProfileName()
        val userAnswer = UserAnswerDemographicTest(
            profileName.toString(),
            question,
            selectedOption,
            osteoporosisPoints,
            osteoarthritisPoints,
            answerEditText
        )

        val existingIndex = userAnswers.indexOfFirst { it.question == question }
        if (existingIndex != -1) {
            userAnswers[existingIndex] = userAnswer // Update jawaban jika sudah ada
        } else {
            userAnswers.add(userAnswer) // Tambah jawaban baru
        }
    }

    private fun calculateScore() {
        osteoporosisTotalScore = userAnswers.sumOf { it.osteoporosisPoints }
        osteoarthritisTotalScore = userAnswers.sumOf { it.osteoarthritisPoints }
        val session = SessionManager(this)
        val userId = auth.currentUser?.uid.toString()
        val profileId = session.getProfileId()
        condition = when {
            osteoarthritisTotalScore < 30 && osteoporosisTotalScore < 30 -> "Sehat"
            osteoarthritisTotalScore >= 30 && osteoporosisTotalScore < 30 -> "Osteoarthritis"
            osteoarthritisTotalScore < 30 && osteoporosisTotalScore >= 30 -> "Osteoporosis"
            else -> "Osteoporosis dan Osteoarthritis"
        }

        val scoreMessage = if (condition == "Sehat") {
            "Kamu Sehat"
        } else { "Kamu diprediksi terjangkit $condition" }

        userAnswers.forEach { userAnswer ->
            val question = adapter.currentList.find { it.question == userAnswer.question }
            question?.let {
                val answerToSave = if (userAnswer.selectedOption.isNotEmpty()) {
                    userAnswer.selectedOption
                } else {
                    userAnswer.answerText // Jawaban dari EditText jika tidak memilih option
                }

                viewModel.getSaveAnswerDemographyTest(
                    userId.toString(),
                    profileId,
                    it.id,
                    answerToSave.toString()
                )
            }
        }


        viewModel.getSaveScore(
            userId.toString(),
            profileId,
            0,
            0,
            0,
            osteoporosisTotalScore,
            osteoarthritisTotalScore
        )

        session.saveConditionSession(condition)

        viewModel.getSaveCondition(userId, profileId, condition)

        showResultDialog(scoreMessage)
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Hasil Pre-Test")
            .setMessage(message)
            .setPositiveButton("OK") {_, _ ->
                startActivity(Intent(this, MainActivity::class.java))
            }
            .show()
    }

    private fun submitAnswers() {
        val questions = adapter.currentList.toMutableList()

        val firstInvalidIndex = questions.indexOfFirst { question ->
            val answer = userAnswers.find { it.question == question.question }
            answer == null || (answer.selectedOption.isEmpty() && answer.answerText.isBlank())
        }

        if (firstInvalidIndex != -1) {
            questions[firstInvalidIndex].isError = true
            adapter.submitList(questions.toList()) {
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition(firstInvalidIndex)
                }
            }


            Toast.makeText(this, "Harap lengkapi semua pertanyaan", Toast.LENGTH_SHORT).show()
            return
        }


        // Semua terisi, lanjut simpan
        calculateScore()
    }

}