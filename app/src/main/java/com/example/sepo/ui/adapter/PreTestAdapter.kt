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
    private val onOptionSelected: (PreTestResponseItem, String, Int, Int) -> Unit
) : ListAdapter<PreTestResponseItem, PreTestAdapter.QuestionViewHolder>(DIFF_CALLBACK) {

    private val selectedAnswers: MutableMap<Int, String> = mutableMapOf()

    inner class QuestionViewHolder(val binding: ItemQuestionsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val questionItem = getItem(position)
        val binding = holder.binding

        binding.title.text = "Pertanyaan ${position + 1}"
        binding.question.text = questionItem.question ?: ""
        binding.answer.removeAllViews()

        val selectedOption = selectedAnswers[position]

        questionItem.options?.forEach { option ->
            val optionText = option?.text ?: return@forEach
            val behavePoints = option.behavePoints ?: 0
            val hrqPoints = option.hrqPoints ?: 0

            val radioButton = RadioButton(binding.root.context).apply {
                text = optionText
                isChecked = selectedOption == optionText
                setOnClickListener {
                    selectedAnswers[position] = optionText
                    onOptionSelected(questionItem, optionText, behavePoints, hrqPoints)
                    notifyItemChanged(position)
                }
            }

            binding.answer.addView(radioButton)
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
