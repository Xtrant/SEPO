package com.example.sepo.ui.consult

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.data.response.Message
import com.example.sepo.data.response.MessageList
import com.example.sepo.databinding.ActivityReplyDoctorChatBinding
import com.example.sepo.ui.adapter.FirebaseMessageAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.Date

class ReplyDoctorChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReplyDoctorChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: FirebaseMessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReplyDoctorChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.database

        val firebaseUser = auth.currentUser
        val uid = firebaseUser?.uid

        val userId = intent.getStringExtra("user_id")
        val userName = intent.getStringExtra("user_name")

        val doctorId = intent.getStringExtra("doctor_id")
        val doctorName = intent.getStringExtra("doctor_name")

        binding.tvName.text = userName

        val combineKey = "${doctorId}_${userId}"

        val messagesRef = db.reference.child(MESSAGES_CHILD).child(combineKey)
        val messageList = db.reference.child("list_chat").child(doctorId.toString()).child(userId.toString())

        binding.sendButton.setOnClickListener {
            val friendlyMessage = Message(
                uid,
                doctorId.toString(),
                doctorName,
                userId,
                userName,
                binding.messageEditText.text.toString(),
                Date().time,
            )

            val listMessage = MessageList(
                userId.toString(),
                userName,
                doctorId,
                doctorId.toString(),
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
                options, doctorId.toString()
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