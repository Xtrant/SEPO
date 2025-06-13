package com.example.sepo.ui.profile

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.sepo.R
import com.example.sepo.databinding.ActivityEditProfileBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.utils.SessionManager
import com.example.sepo.utils.showLoading
import com.example.sepo.utils.showToast
import com.google.firebase.auth.FirebaseAuth

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        val session = SessionManager(this)
        val profileId = session.getProfileId()

        observeViewModel()

        binding.btnCreateProfile.setOnClickListener {
            val name = binding.etUsername.text.toString()
            val age = binding.etAge.text.toString()
            val selectedId = binding.rgGender.checkedRadioButtonId
            val gender = when (selectedId) {
                R.id.rb_male -> "Laki-laki"
                R.id.rb_female -> "Perempuan"
                else -> {
                    Toast.makeText(this, "Silakan pilih jenis kelamin", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            val weight = binding.etBeratBadan.text.toString()
            val height = binding.etTinggiBadan.text.toString()

            if (name.isNotBlank() && age.isNotBlank() && gender.isNotBlank() && height.isNotBlank() && weight.isNotBlank()) {
                session.editSession()
                session.saveSession(profileId, name)

                viewModel.editProfile(profileId, name, age, gender, weight, height)



                finish()
            } else {
                Toast.makeText(this, "Lengkapi semua Data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val uId = auth.currentUser?.uid
        val session = SessionManager(this)
        val profileId = session.getProfileId()

        viewModel.getProfilesById(uId.toString(), profileId)
    }

    private fun observeViewModel() {
        viewModel.profileSelectResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    binding.etUsername.setText(result.data.profileName.toString())
                    binding.etAge.setText(result.data.age.toString())
                    binding.etBeratBadan.setText(result.data.beratBadan.toString())
                    binding.etTinggiBadan.setText(result.data.tinggiBadan.toString())

                    val gender = result.data.gender.toString()

                    when (gender) {
                        "Perempuan" -> binding.rgGender.check(R.id.rb_female)
                        "Laki-laki" -> binding.rgGender.check(R.id.rb_male)
                    }

                }

                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    showToast(this, result.error)
                }
            }
        }

        viewModel.editProfileResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    showToast(this, "Profil berhasil di update")

                }

                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    showToast(this, result.error)
                }
            }

        }
    }
}