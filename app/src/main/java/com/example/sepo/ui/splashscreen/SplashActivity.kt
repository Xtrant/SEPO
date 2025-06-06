package com.example.sepo.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.sepo.R
import com.example.sepo.databinding.ActivitySplashBinding
import com.example.sepo.ui.authentication.LoginActivity
import com.example.sepo.ui.main.MainActivity
import com.example.sepo.ui.onboarding.OnboardingActivity
import com.example.sepo.ui.profile.SelectProfileActivity
import com.example.sepo.utils.SessionManager
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Animasi fade-in
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        binding.splashContainer.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            val session = SessionManager(this)

            // Jika ini pertama kali aplikasi dijalankan sejak install
            if (session.isFirstAppRun()) {
                session.setFirstAppRun(false) // Jangan jalankan lagi di masa depan
                FirebaseAuth.getInstance().signOut() // Clear UID yang otomatis muncul
            }

            // Jalankan onboarding jika belum pernah tampil
            if (session.isFirstTimeLaunch()) {
                session.setFirstTimeLaunch(false)
                startActivity(Intent(this, OnboardingActivity::class.java))
            } else {
                val user = FirebaseAuth.getInstance().currentUser
                if (user == null || user.isAnonymous) {
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    if (session.getProfileId() == -1) {
                        startActivity(Intent(this, SelectProfileActivity::class.java))
                    } else {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            }

            finish()
        }, 1500)
    }
}

