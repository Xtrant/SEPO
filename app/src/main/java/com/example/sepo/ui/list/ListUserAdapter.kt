package com.example.sepo.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.ListUserResponseItem
import com.example.sepo.databinding.ListUserBinding

class ListUserAdapter : ListAdapter<ListUserResponseItem, ListUserAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ListUserBinding ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user : ListUserResponseItem) {
            binding.idUser.text = user.userId
            binding.emailUser.text = user.email
            binding.createdAt.text = user.createdAt
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListUserBinding.inflate(LayoutInflater.from(parent.context))
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListUserResponseItem>() {
            override fun areItemsTheSame(
                oldItem: ListUserResponseItem,
                newItem: ListUserResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListUserResponseItem,
                newItem: ListUserResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


}