package com.example.sepo.ui.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.DemographicTestResponseItem
import com.example.sepo.data.response.UserAnswerDemographicTest
import com.example.sepo.databinding.ItemQuestionsBinding

class DemographicAdapter(
    private val userAnswers: MutableList<UserAnswerDemographicTest>,
    private val onOptionSelected: (String, String, Int, Int, String) -> Unit
) : ListAdapter<DemographicTestResponseItem, DemographicAdapter.QuestionViewHolder>(DIFF_CALLBACK) {

    inner class QuestionViewHolder(val binding: ItemQuestionsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var currentWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val questionItem = getItem(position)
        val binding = holder.binding

        // Reset tampilan
        binding.answer.removeAllViews()
        binding.etAnswer.visibility = View.GONE
        binding.answer.visibility = View.GONE

        binding.title.text = "Pertanyaan ${position + 1}"
        binding.question.text = questionItem.question

        val userAnswer = userAnswers.find { it.question == questionItem.question }

        if (questionItem.options.isNotEmpty()) {
            binding.answer.visibility = View.VISIBLE

            questionItem.options.forEach { optionText ->
                val radioButton = RadioButton(binding.root.context).apply {
                    text = optionText.text ?: ""
                    isChecked = userAnswer?.selectedOption == text

                    setOnClickListener {
                        val osteoporosisPoints = optionText.pointsOsteoporosis
                        val osteoarthritisPoints = optionText.pointsArthritis
                        val answerText = optionText.text ?: "Invalid Question"

                        val newAnswer = UserAnswerDemographicTest(
                            question = questionItem.question,
                            selectedOption = answerText,
                            osteoporosisPoints = osteoporosisPoints,
                            osteoarthritisPoints = osteoarthritisPoints,
                            answerText = "",
                            profileName = null
                        )

                        val existingIndex = userAnswers.indexOfFirst { it.question == questionItem.question }
                        if (existingIndex != -1) {
                            userAnswers[existingIndex] = newAnswer
                        } else {
                            userAnswers.add(newAnswer)
                        }

                        onOptionSelected(
                            questionItem.question,
                            answerText,
                            osteoporosisPoints,
                            osteoarthritisPoints,
                            ""
                        )
                    }
                }

                binding.answer.addView(radioButton)
            }
        } else {
            binding.etAnswer.visibility = View.VISIBLE

            // Hapus listener sebelumnya
            holder.currentWatcher?.let {
                binding.etAnswer.removeTextChangedListener(it)
            }

            val correctText = userAnswer?.answerText ?: ""
            if (binding.etAnswer.text.toString() != correctText) {
                binding.etAnswer.setText(correctText)
            }

            val watcher = object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val answerText = s.toString()

                    val newAnswer = UserAnswerDemographicTest(
                        question = questionItem.question,
                        selectedOption = "",
                        osteoporosisPoints = 0,
                        osteoarthritisPoints = 0,
                        answerText = answerText,
                        profileName = null
                    )

                    val existingIndex = userAnswers.indexOfFirst { it.question == questionItem.question }
                    if (existingIndex != -1) {
                        userAnswers[existingIndex] = newAnswer
                    } else {
                        userAnswers.add(newAnswer)
                    }

                    onOptionSelected(questionItem.question, "", 0, 0, answerText)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            }

            binding.etAnswer.addTextChangedListener(watcher)
            holder.currentWatcher = watcher
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DemographicTestResponseItem>() {
            override fun areItemsTheSame(
                oldItem: DemographicTestResponseItem,
                newItem: DemographicTestResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DemographicTestResponseItem,
                newItem: DemographicTestResponseItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
