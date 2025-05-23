package com.example.sepo.ui.consult

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.databinding.ActivityConsultBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.DoctorAdapter
import com.example.sepo.ui.list.ListUserViewModel
import com.example.sepo.utils.showLoading

class ConsultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConsultBinding
    private val viewModel: ListUserViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityConsultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeViewModel()

        viewModel.listDoctor()
    }

    private fun observeViewModel() {
        viewModel.resultListDoctor.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    val adapter = DoctorAdapter(result.data) {doctor ->
                        val intent = Intent(this, LiveChatActivity::class.java)
                        intent.putExtra("id_doctor", doctor.id)
                        startActivity(intent)
                    }
                    binding.recyclerView.layoutManager = LinearLayoutManager(this)
                    binding.recyclerView.adapter = adapter
                }


                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    Toast.makeText(this, "Gagal membuat profil: ${result.error}", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }


}