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
import com.example.sepo.data.response.PostTestResponseItem
import com.example.sepo.data.response.UserAnswerPostTest
import com.example.sepo.databinding.ActivityPostTestBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.PostTestAdapter
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
import com.google.firebase.auth.FirebaseAuth

class PostTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostTestBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: PostTestAdapter
    private val userAnswers = mutableListOf<UserAnswerPostTest>()
    private var correctCount = 0
    private var score = 0
    private val viewModel: TestViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPostTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance() // Inisialisasi FirebaseAuth

        observeViewModel()

        viewModel.getPostTest()

        setupRecyclerView()

        val uId = auth.currentUser?.uid

        val session = SessionManager(this)
        val profileId = session.getProfileId()

        setupRecyclerView()

        binding.btnSbmt.setOnClickListener {
            viewModel.getSaveProfileStatus(uId.toString(), profileId, null, 1, null)
            submitAnswers()
        }

    }

    private fun observeViewModel() {
        viewModel.postTestResult.observe(this) { result ->
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
                    Toast.makeText(this, "Gagal membuat profil: ${result.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = PostTestAdapter { question, selectedOption ->
            // Handle jawaban user
            saveUserAnswer(question, selectedOption)
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PostTestActivity)
            adapter = this@PostTestActivity.adapter
        }
    }

    private fun saveUserAnswer(question: PostTestResponseItem, selectedOption: String) {
        val session = SessionManager(this)
        val profileName = session.getProfileName()
        val userAnswer = UserAnswerPostTest(profileName.toString(), question.question.toString(), selectedOption)

        val existingIndex = userAnswers.indexOfFirst { it.question == question.question }
        if (existingIndex != -1) {
            userAnswers[existingIndex] = userAnswer // Update jawaban jika sudah ada
        } else {
            userAnswers.add(userAnswer) // Tambah jawaban baru
        }
    }

    private fun calculateScore() {
        correctCount = 0 // Reset jawaban benar sebelum perhitungan ulang
        val session = SessionManager(this)
        val userId = auth.currentUser?.uid
        val profileId = session.getProfileId()
        val totalQuestions = adapter.currentList.size

        userAnswers.forEach { userAnswer ->
            val question = adapter.currentList.find { it.question == userAnswer.question }
            if (question?.correctAnswer == userAnswer.selectedOption) {
                correctCount++
            }
            question?.let {
                viewModel.getSaveAnswerPostTest(
                    userId.toString(),
                    profileId,
                    it.id,
                    userAnswer.selectedOption
                )
            }
        }

        score = correctCount * 100 / totalQuestions

        viewModel.getSaveScore(
            userId.toString(),
            profileId,
            score,
            null,
            null,
            null,
            null
        )

        val scoreMessage = "Skor Anda = $score"

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


        // Semua terisi, lanjut simpan
        calculateScore()
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Hasil Post-Test")
            .setMessage(message)
            .setPositiveButton("OK") {_,_ -> finish()}
            .show()
    }

}
