package com.example.sepo.ui.adapter

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.sepo.R
import com.example.sepo.data.response.Message
import com.example.sepo.databinding.ItemMessageBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

class FirebaseMessageAdapter(
    options: FirebaseRecyclerOptions<Message>,
    private val currentUserName: String?
) : FirebaseRecyclerAdapter<Message, FirebaseMessageAdapter.MessageViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_message, parent, false)
        val binding = ItemMessageBinding.bind(view)
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
            binding.tvMessage.text = item.text
            updateBubblePosition(item.name)
            binding.tvTimestamp.text = item.timestamp?.let {
                DateUtils.getRelativeTimeSpanString(it)
            }

            // Tampilkan nama & foto hanya kalau bukan diri sendiri
            if (currentUserName == item.name) {
                binding.tvMessenger.visibility = View.GONE
                binding.ivMessenger.visibility = View.GONE
            } else {
                binding.tvMessenger.visibility = View.VISIBLE
                binding.tvMessenger.text = item.name
                binding.ivMessenger.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .circleCrop()
                    .into(binding.ivMessenger)
            }
        }


        private fun updateBubblePosition(userName: String?) {
            val constraintLayout = binding.rootLayout
            val constraintSet = androidx.constraintlayout.widget.ConstraintSet()
            constraintSet.clone(constraintLayout)

            if (currentUserName == userName && userName != null) {
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