package com.example.sepo.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.R

import com.example.sepo.data.response.Message
import com.example.sepo.databinding.ItemMessageBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FirebaseMessageAdapter(
    options: FirebaseRecyclerOptions<Message>,
    private val currentProfileId: String?
) : FirebaseRecyclerAdapter<Message, FirebaseMessageAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MessageViewHolder,
        position: Int,
        model: Message
    ) {
        holder.bind(model)
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.tvMessage.text = item.message
            updateBubblePosition(item.fromProfileId)
            binding.tvTimestamp.text = item.timestamp?.let {
                DateUtils.getRelativeTimeSpanString(it)
            }

            // Tampilkan nama & foto hanya kalau bukan diri sendiri
            if (currentProfileId == item.fromProfileId) {
                binding.tvMessenger.visibility = View.GONE
                binding.ivMessenger.visibility = View.GONE
            } else {
                binding.tvMessenger.visibility = View.VISIBLE
                binding.tvMessenger.text = item.fromProfileName
                binding.ivMessenger.visibility = View.VISIBLE
            }
        }


        private fun updateBubblePosition(profileId: String?) {
            val constraintLayout = binding.rootLayout
            val constraintSet = androidx.constraintlayout.widget.ConstraintSet()
            constraintSet.clone(constraintLayout)

            if (currentProfileId == profileId && profileId != null) {
                // Chat dari user sendiri → Geser ke kanan
                constraintSet.clear(binding.tvMessage.id, androidx.constraintlayout.widget.ConstraintSet.START)
                constraintSet.connect(
                    binding.tvMessage.id,
                    androidx.constraintlayout.widget.ConstraintSet.END,
                    androidx.constraintlayout.widget.ConstraintSet.PARENT_ID,
                    androidx.constraintlayout.widget.ConstraintSet.END,
                    16
                )

                binding.tvMessage.setBackgroundResource(R.drawable.rounded_message_blue)

            } else {
                // Chat dari orang lain → Geser ke kiri
                constraintSet.clear(binding.tvMessage.id, androidx.constraintlayout.widget.ConstraintSet.END)
                constraintSet.connect(
                    binding.tvMessage.id,
                    androidx.constraintlayout.widget.ConstraintSet.START,
                    binding.tvMessenger.id,
                    androidx.constraintlayout.widget.ConstraintSet.START,
                    16
                )

                binding.tvMessage.setBackgroundResource(R.drawable.rounded_message_yellow)
            }

            constraintSet.applyTo(constraintLayout)
        }
    }
}