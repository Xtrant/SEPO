package com.example.sepo.ui.profile


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
import com.example.sepo.databinding.ActivitySelectProfileBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.ProfileAdapter
import com.example.sepo.ui.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SelectProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectProfileBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySelectProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        observeProfiles()

        binding.btnAddProfile.setOnClickListener {
            startActivity(Intent(this, CreateProfileActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = auth.currentUser?.uid

        viewModel.getProfiles(userId.toString())
    }

    private fun observeProfiles() {
        viewModel.profileResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    // bisa tampilkan loading
                }
                is Result.Success -> {
                    val adapter = ProfileAdapter(result.data) { profile ->
                        Toast.makeText(this, "Profil ${profile.profileName} dipilih", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("profile name", profile.profileName)
                        startActivity(intent)
                    }
                    binding.rvProfiles.layoutManager = LinearLayoutManager(this)
                    binding.rvProfiles.adapter = adapter
                }
                is Result.Error -> {
                    Toast.makeText(this, "Gagal load profil: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
