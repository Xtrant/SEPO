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



        setupRecyclerView()

        binding.btnSbmt.setOnClickListener {

            submitAnswers()
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
            null,
            behaveTotalScore,
            hrqTotalScore,
            null,
            null
        )

        showResultDialog(scoreMessage)
    }
    private fun submitAnswers() {
        val questions = adapter.currentList.toMutableList()

        val firstInvalidIndex = questions.indexOfFirst { question ->
            val answer = userAnswers.find { it.question == question.question }
            answer == null || (answer.selectedOption.isEmpty())
        }

        if (firstInvalidIndex != -1) {
            adapter.submitList(questions.toList()) {
                binding.recyclerView.post {
                    binding.recyclerView.scrollToPosition(firstInvalidIndex)
                }
            }


            Toast.makeText(this, "Harap lengkapi semua pertanyaan", Toast.LENGTH_SHORT).show()
            return
        }
        auth = FirebaseAuth.getInstance()
        val uId = auth.currentUser?.uid

        val session = SessionManager(this)
        val profileId = session.getProfileId()

        viewModel.getSaveProfileStatus(uId.toString(), profileId, 1, null, null)
        // Semua terisi, lanjut simpan
        calculateScore()
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Hasil Pre-Test")
            .setMessage(message)
            .setPositiveButton("OK") {_,_ -> finish()

            }
            .show()
    }
}



