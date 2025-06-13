package com.example.sepo.ui.exercise

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.databinding.ActivityDetailExerciseBinding
import com.example.sepo.databinding.ActivityEducationBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.VideoAdapter
import com.example.sepo.ui.list.ListUserViewModel
import com.example.sepo.ui.test.TestViewModel
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
import com.google.firebase.auth.FirebaseAuth
import kotlin.getValue

class DetailExerciseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailExerciseBinding
    private val viewModel: ListUserViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private val viewModelProfile: TestViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        viewModel.listExercise()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.resultListExercise.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    val adapter = VideoAdapter(
                        result.data,
                        activity = this,
                        fullscreenContainer = binding.fullscreenContainer,
                        viewModel = viewModelProfile,
                        sessionManager = SessionManager(this))
                    binding.rvMateri.layoutManager = LinearLayoutManager(this)
                    binding.rvMateri.adapter = adapter
                }

                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                }
            }
        }
    }

}