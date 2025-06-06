package com.example.sepo.ui.consult

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.data.response.MessageList
import com.example.sepo.databinding.ActivityShowDoctorChatBinding
import com.example.sepo.ui.adapter.ShowDoctorChatAdapter
import com.example.sepo.ui.authentication.LoginActivity
import com.example.sepo.utils.SessionManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ShowDoctorChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowDoctorChatBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: ShowDoctorChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityShowDoctorChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = Firebase.database

        var doctorId = intent.getStringExtra("doctor_id")

        val manager = LinearLayoutManager(this)
        binding.rvListChat.layoutManager = manager

        val messagesRef = db.getReference("list_chat").child(doctorId.toString()).orderByChild("timestamp").limitToLast(30)
        val options = FirebaseRecyclerOptions.Builder<MessageList>()
            .setQuery(messagesRef, MessageList::class.java)
            .build()

         adapter = ShowDoctorChatAdapter(options, doctorId.toString()) { message ->
             val session = SessionManager(this)
             val doctorName = session.getProfileName()
             doctorId = message.doctorId
             val userId = message.userId
             val userName = message.userName
             val intent = Intent(this, ReplyDoctorChatActivity::class.java)
             intent.putExtra("doctor_id", doctorId)
             intent.putExtra("user_id", userId)
             intent.putExtra("doctor_name", doctorName)
             intent.putExtra("user_name", userName)
             startActivity(intent)
        }

        binding.rvListChat.adapter = adapter

        setSupportActionBar(binding.toolbar)

        supportActionBar?.apply {
            title = "List Chat Dokter"
            setDisplayHomeAsUpEnabled(true)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter.startListening()
        adapter.notifyDataSetChanged()
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.show_list_chat_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                signOut()
                true
            }
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun signOut() {
        lifecycleScope.launch {
            val credentialManager = CredentialManager.create(this@ShowDoctorChatActivity)
            val session = SessionManager(this@ShowDoctorChatActivity)
            session.clearSession()
            FirebaseAuth.getInstance().signOut()
            credentialManager.clearCredentialState(ClearCredentialStateRequest())
            startActivity(Intent(this@ShowDoctorChatActivity, LoginActivity::class.java))
            finish()
        }
    }

}