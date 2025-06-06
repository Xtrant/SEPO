package com.example.sepo.ui.profile


import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.example.sepo.ui.consult.ShowDoctorChatActivity
import com.example.sepo.ui.main.MainActivity
import com.example.sepo.ui.test.DemographicActivity
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
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
                    showLoading(true, binding.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    binding.btnAddProfile.visibility = View.VISIBLE
                    val adapter = ProfileAdapter(result.data) { profile ->
                        Toast.makeText(
                            this,
                            "Profil ${profile.profileName} dipilih",
                            Toast.LENGTH_SHORT
                        ).show()
                        val session = SessionManager(this)
                        session.saveSession(profile.profileId, profile.profileName)
                        val uId = auth.currentUser?.uid

                        if (uId == "tX7f0bYtfBO2Hl69wnX3QtN1KWl2") {
                            val intent = Intent(this, ShowDoctorChatActivity::class.java)
                            intent.putExtra("doctor_id", profile.dokter_id)
                            startActivity(intent)

                        } else {

                            if (profile.kondisi.isNotEmpty()) {
                                session.saveConditionSession(profile.kondisi)
                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            } else {
                                val intent = Intent(this, DemographicActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)

                            }
                        }
                    }
                    binding.rvProfiles.layoutManager = LinearLayoutManager(this)
                    binding.rvProfiles.adapter = adapter
                }

                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    Toast.makeText(this, "Gagal load profil: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
