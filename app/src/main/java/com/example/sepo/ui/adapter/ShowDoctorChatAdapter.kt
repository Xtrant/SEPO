package com.example.sepo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.MessageList
import com.example.sepo.databinding.ItemProfileBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class ShowDoctorChatAdapter(
    options: FirebaseRecyclerOptions<MessageList>,
    private val currentId: String,
    private val onItemClick: (MessageList) -> Unit

) : FirebaseRecyclerAdapter<MessageList, ShowDoctorChatAdapter.MessageViewHolder>(options) {

    inner class MessageViewHolder(val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageList) {
       

            if (currentId == message.fromProfileId) {
                binding.tvName.text = message.userName
                binding.tvAge.text = "You: ${message.message}"
            } else {
                binding.tvName.text = message.userName
                binding.tvAge.text = "${message.userName}: ${message.message}"
            }

            binding.root.setOnClickListener {
                onItemClick(message)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemProfileBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int, model: MessageList) {
        holder.bind(model)
    }
}
