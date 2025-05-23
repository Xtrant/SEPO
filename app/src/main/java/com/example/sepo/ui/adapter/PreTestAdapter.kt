package com.example.sepo.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.PreTestResponseItem
import com.example.sepo.databinding.ItemQuestionsBinding

class PreTestAdapter(
    private val onOptionSelected: (PreTestResponseItem, String, Int, Int) -> Unit // Kirim jawaban yang dipilih (opsi string)
) : ListAdapter<PreTestResponseItem, PreTestAdapter.QuestionViewHolder>(DIFF_CALLBACK) {

    inner class QuestionViewHolder(val binding: ItemQuestionsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val questionItem = getItem(position)

        holder.binding.title.text = "Pertanyaan ${position + 1}"
        holder.binding.question.text = questionItem.question ?: ""
        holder.binding.answer.removeAllViews()

        questionItem.options?.forEach { optionText ->
            val radioButton = RadioButton(holder.binding.root.context).apply {
                text = optionText?.text ?: ""
                isChecked = false

                setOnClickListener {
                    val behavePoints = optionText?.behavePoints ?: 0
                    val hrqPoints = optionText?.hrqPoints ?: 0
                    val answerText = optionText?.text ?: "Jawaban tidak ditemukan"
                    onOptionSelected(questionItem,answerText, behavePoints, hrqPoints)
                }
            }
            holder.binding.answer.addView(radioButton)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PreTestResponseItem>() {
            override fun areItemsTheSame(oldItem: PreTestResponseItem, newItem: PreTestResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: PreTestResponseItem, newItem: PreTestResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}