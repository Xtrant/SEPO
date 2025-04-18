package com.example.sepo.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sepo.R
import com.example.sepo.databinding.ActivityCreateProfileBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.google.firebase.auth.FirebaseAuth

class CreateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        binding.btnCreateProfile.setOnClickListener {
            val name = binding.etName.text.toString()
            val age = binding.etAge.text.toString().toIntOrNull()
            val gender = binding.etGender.text.toString()

            if (name.isNotBlank() && age != null && gender.isNotBlank()) {
                viewModel.createProfile(uid, name, age, gender)
            } else {
                Toast.makeText(this, "Lengkapi semua field", Toast.LENGTH_SHORT).show()
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.createResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    Toast.makeText(this, "Membuat profil...", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    Toast.makeText(this, "Profil berhasil dibuat", Toast.LENGTH_SHORT).show()
                    finish() // kembali ke halaman sebelumnya / bisa diarahkan ke halaman pilih profil
                }
                is Result.Error -> {
                    Toast.makeText(this, "Gagal membuat profil: ${result.error}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
