package com.example.sepo.ui.consult

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.data.response.Message
import com.example.sepo.data.response.MessageList
import com.example.sepo.databinding.ActivityLiveChatBinding
import com.example.sepo.ui.adapter.FirebaseMessageAdapter
import com.example.sepo.utils.SessionManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class LiveChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLiveChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FirebaseMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLiveChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.database

        val firebaseUser = auth.currentUser
        val uid = firebaseUser?.uid

        val session = SessionManager(this)
        val profileId = session.getProfileId()
        val profileName = session.getProfileName()

        val doctorId = intent.getStringExtra("id_doctor")
        val doctorName = intent.getStringExtra("name_doctor")

        binding.tvName.text = doctorName

        val combineKey = "${doctorId}_${profileId}"

        val messagesRef = db.reference.child(MESSAGES_CHILD).child(combineKey)
        val messageList = db.reference.child("list_chat").child(doctorId.toString()).child(profileId.toString())

        binding.sendButton.setOnClickListener {
            val friendlyMessage = Message(
                uid,
                profileId.toString(),
                profileName,
                doctorId,
                doctorName,
                binding.messageEditText.text.toString(),
                Date().time,
            )

            val listMessage = MessageList(
                profileId.toString(),
                profileName,
                doctorId,
                profileId.toString(),
                binding.messageEditText.text.toString(),
                Date().time,
            )
            messagesRef.push().setValue(friendlyMessage) { error, _ ->
                if (error != null) {
                    Toast.makeText(
                        this,
                        getString(R.string.send_error) + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(this, getString(R.string.send_success), Toast.LENGTH_SHORT)
                        .show()

                    messageList.setValue(listMessage)
                    binding.messageRecyclerView.scrollToPosition(adapter.itemCount - 1)
                }
            }
            binding.messageEditText.setText("")
        }

        val manager = LinearLayoutManager(this)
        manager.stackFromEnd = true
        binding.messageRecyclerView.layoutManager = manager

        val showMessage = db.getReference(MESSAGES_CHILD).child(combineKey)

        val options = FirebaseRecyclerOptions.Builder<Message>()
            .setQuery(showMessage, Message::class.java)
            .build()
        if (firebaseUser != null) {
            adapter = FirebaseMessageAdapter(
                options, profileId.toString()
            )
        }
        binding.messageRecyclerView.adapter = adapter
    }

    public override fun onResume() {
        super.onResume()
        adapter.startListening()
    }

    public override fun onPause() {
        adapter.stopListening()
        super.onPause()
    }


    companion object {
        const val MESSAGES_CHILD = "messages"
    }
}