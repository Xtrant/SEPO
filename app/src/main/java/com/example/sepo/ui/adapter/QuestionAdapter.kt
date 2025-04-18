package com.example.sepo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.AnswerOption
import com.example.sepo.data.response.Question
import com.example.sepo.databinding.ItemQuestionsBinding

class QuestionAdapter(
    private val isPreTest: Boolean, // True untuk Pre-Test, False untuk Post-Test
    private val onOptionSelected: (Question, AnswerOption) -> Unit // Callback untuk jawaban yang dipilih
) : ListAdapter<Question, QuestionAdapter.QuestionViewHolder>(DIFF_CALLBACK) {

    inner class QuestionViewHolder(val binding: ItemQuestionsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = getItem(position)

        holder.binding.title.text = "Pertanyaan ${position + 1}"
        holder.binding.question.text = question.question
        holder.binding.answer.removeAllViews()

        question.options.forEach { option ->
            val radioButton = RadioButton(holder.binding.root.context).apply {
                text = option.text
                isChecked = false // Set default false

                setOnClickListener {
                    onOptionSelected(question, option) // Kirim jawaban ke Activity
                }
            }
            holder.binding.answer.addView(radioButton)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Question>() {
            override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem.id == newItem.id // Pastikan Question punya ID unik
            }

            override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
                return oldItem == newItem
            }
        }
    }
}





