package com.example.sepo.ui.test

import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.example.sepo.databinding.ActivityPreTestBinding
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.QuestionAdapter
import com.example.sepo.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.getValue

class PreTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreTestBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var adapter: QuestionAdapter
    private val userAnswers = mutableMapOf<String, UserAnswer>() // Menyimpan jawaban user
    private var totalScore = 0
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

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupRecyclerView()
        loadQuestionsFromFirestore()
//
//        binding.btnSbmt.setOnClickListener {
//            saveUserAnswersToFirestore()
//            calculateScore()
//        }
    }

    private fun setupRecyclerView() {
        adapter = QuestionAdapter(isPreTest = true) { question, selectedOption ->
            userAnswers[question.id] = UserAnswer(
                username = auth.currentUser?.displayName ?: "Guest",
                question = question.question,
                selectedOption = selectedOption.text,
                points = selectedOption.points
            )
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@PreTestActivity)
            adapter = this@PreTestActivity.adapter
        }
    }

    private fun loadQuestionsFromFirestore() {
        db.collection("pre_test") // Ambil dari koleksi yang benar
            .get()
            .addOnSuccessListener { result ->
                val questions = result.map { doc ->
                    val optionsList = doc.get("options") as? List<Map<String, Any>> ?: emptyList()
                    val options = optionsList.map {
                        AnswerOption(
                            text = it["text"] as? String ?: "",
                            points = (it["points"] as? Number)?.toInt() ?: 0
                        )
                    }

                    Question(
                        id = doc.id,
                        question = doc.getString("question") ?: "",
                        options = options
                    )
                }

                adapter.submitList(questions)
                if (questions.isNotEmpty()) {
                    binding.btnSbmt.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Log.e("Firestore", "Gagal mengambil data", e)
            }
    }

    private fun saveUserAnswersToFirestore() {
        val userId = auth.currentUser?.uid ?: return
        val userRef = db.collection("user_answers").document(userId)

        userAnswers.forEach { (_, userAnswer) ->
            val answerData = hashMapOf(
                "username" to userAnswer.username,
                "question" to userAnswer.question,
                "selectedOption" to userAnswer.selectedOption,
                "points" to userAnswer.points,
                "timestamp" to FieldValue.serverTimestamp()
            )

            userRef.collection("pre_test_answers").document(userAnswer.question)
                .set(answerData)
        }
    }

//    private fun calculateScore() {
//        totalScore = userAnswers.values.sumOf { it.points } // Menjumlahkan semua poin dari jawaban user
//
//        val scoreMessage = "Total Skor Pre-Test: $totalScore"
//
//        showResultDialog(scoreMessage)
//        saveUserScoreToFirestore(totalScore)
//    }
//
//    private fun showResultDialog(message: String) {
//        AlertDialog.Builder(this)
//            .setTitle("Hasil Pre-Test")
//            .setMessage(message)
//            .setPositiveButton("OK", null)
//            .show()
//    }
//
//    private fun saveUserScoreToFirestore(score: Int) {
//        val userId = auth.currentUser?.uid ?: return
//        val userRef = db.collection("user_answers").document(userId)
//
//        val scoreData = hashMapOf(
//            "totalScore" to score,
//            "timestamp" to FieldValue.serverTimestamp()
//        )
//
//        userRef.collection("pre_test_results").document("score")
//            .set(scoreData)
//            .addOnSuccessListener {
//                binding.progressBar.visibility = View.GONE
//            }
//            .addOnFailureListener { e ->
//                binding.progressBar.visibility = View.GONE
//                Log.e("Firestore", "Gagal menyimpan skor", e)
//            }
//    }
}
