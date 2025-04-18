package com.example.sepo.ui.test

import android.os.Bundle
import android.util.Log
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
import com.example.sepo.data.response.AnswerOption
import com.example.sepo.data.response.Question
import com.example.sepo.data.response.UserAnswer
import com.example.sepo.databinding.ActivityPostTestBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.PostTestAdapter
import com.example.sepo.ui.adapter.QuestionAdapter
import com.example.sepo.ui.profile.ProfileViewModel
import com.example.sepo.utils.showLoading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue

class PostTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostTestBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: PostTestAdapter
    private val userAnswers = mutableListOf<UserAnswer>()
    private var correctCount = 0
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
        db = FirebaseFirestore.getInstance() // Inisialisasi Firestore
        auth = FirebaseAuth.getInstance() // Inisialisasi FirebaseAuth

        observeViewModel()

        viewModel.getPostTest()

        setupRecyclerView()

        binding.btnSbmt.setOnClickListener {
            saveUserAnswersToFirestore()
            calculateScore()
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
                    adapter.submitList(result.data)
                }
                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    Toast.makeText(this, "Gagal membuat profil: ${result.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

//    private fun loadQuestionsFromFirestore() {
//        db.collection("post_test") // Ambil dari koleksi "post_test"
//            .get()
//            .addOnSuccessListener { result ->
//                val questions = result.map { doc ->
//                    val options = doc.get("options") as? List<String> ?: emptyList()
//                    val correctAnswerIndex = (doc.get("correctAnswerIndex") as? Number)?.toInt() ?: -1
//
//                    Question(
//                        id = doc.id,
//                        question = doc.getString("question") ?: "",
//                        options = options.map { AnswerOption(it, 0) }, // Masukkan teks jawaban ke AnswerOption tanpa poin
//                        correctAnswer = if (correctAnswerIndex in options.indices) options[correctAnswerIndex] else null
//                    )
//                }
//
//                adapter.submitList(questions) // Update tampilan RecyclerView
//
//                binding.progressBar.visibility = View.GONE
//
//                if (questions.isNotEmpty()) {
//                    binding.btnSbmt.visibility = View.VISIBLE
//                }
//            }
//            .addOnFailureListener { e ->
//                binding.progressBar.visibility = View.GONE
//                Log.e("Firestore", "Gagal mengambil data", e)
//            }
//    }



    private fun setupRecyclerView() {
        adapter = PostTestAdapter { question, selectedOption ->
            // Handle jawaban user
//            saveUserAnswer(question, selectedOption)
            Log.d("Selected", "Pertanyaan: ${question.question}, Jawaban: $selectedOption")
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PostTestActivity)
            adapter = this@PostTestActivity.adapter
        }
    }

    private fun saveUserAnswer(question: Question, selectedOption: AnswerOption) {
        val username = auth.currentUser?.displayName ?: "Guest"
        val userAnswer = UserAnswer(username, question.question, selectedOption.text, selectedOption.points)

        val existingIndex = userAnswers.indexOfFirst { it.question == question.question }
        if (existingIndex != -1) {
            userAnswers[existingIndex] = userAnswer // Update jawaban jika sudah ada
        } else {
            userAnswers.add(userAnswer) // Tambah jawaban baru
        }
    }

    private fun saveUserAnswersToFirestore() {
        val userId = auth.currentUser?.uid ?: return  // Pastikan user login
        val userRef = db.collection("user_answers").document(userId)

        // Menyimpan jawaban di sub-collection "answers"
        userAnswers.forEach { userAnswer ->
            val answerData = hashMapOf(
                "username" to userAnswer.username,
                "question" to userAnswer.question,
                "selectedOption" to userAnswer.selectedOption,
                "timestamp" to FieldValue.serverTimestamp()
            )

            // Setiap jawaban disimpan berdasarkan pertanyaan sebagai dokumen unik
            userRef.collection("post_test_answers").document(userAnswer.question)
                .set(answerData)
                .addOnSuccessListener {
                    binding.progressBar.visibility = View.GONE
                }
                .addOnFailureListener { e ->
                    binding.progressBar.visibility = View.GONE
                    Log.e("Firestore", "Gagal menyimpan jawaban", e)
                }
        }
    }

    private fun calculateScore() {
        correctCount = 0 // Reset jawaban benar sebelum perhitungan ulang

        userAnswers.forEach { userAnswer ->
            val question = adapter.currentList.find { it.question == userAnswer.question }
            if (question?.correctAnswer == userAnswer.selectedOption) {
                correctCount++
            }
        }

        val totalQuestions = adapter.currentList.size
        val scoreMessage = "Jawaban benar: $correctCount dari $totalQuestions"

        showResultDialog(scoreMessage)
        saveUserScoreToFirestore(correctCount, totalQuestions)
    }

    private fun showResultDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Hasil Post-Test")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun saveUserScoreToFirestore(correctCount: Int, totalQuestions: Int) {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("user_answers").document(userId)

        val scoreData = hashMapOf(
            "correctCount" to correctCount,
            "totalQuestions" to totalQuestions,
            "timestamp" to FieldValue.serverTimestamp()
        )

        userRef.collection("post_test_results").document("score")
            .set(scoreData)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Log.e("Firestore", "Gagal menyimpan skor", e)
            }
    }
}
