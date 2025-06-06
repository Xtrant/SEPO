package com.example.sepo.ui.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.RadioButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.sepo.data.response.DemographicTestResponseItem
import com.example.sepo.databinding.ItemQuestionsBinding

class DemographicAdapter(
    private val onOptionSelected: (String, String, Int, Int, String) -> Unit // Kirim jawaban yang dipilih (opsi string)
) : ListAdapter<DemographicTestResponseItem, DemographicAdapter.QuestionViewHolder>(DIFF_CALLBACK) {

    inner class QuestionViewHolder(val binding: ItemQuestionsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val binding = ItemQuestionsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val questionItem = getItem(position)

        holder.binding.title.text = "Pertanyaan ${position + 1}"
        holder.binding.question.text = questionItem.question
        holder.binding.answer.removeAllViews()

        if (questionItem.options.isNotEmpty()) {

        questionItem.options.forEach { optionText ->
            val radioButton = RadioButton(holder.binding.root.context).apply {
                text = optionText.text ?: ""
                isChecked = false

                setOnClickListener {
                    val osteoporosisPoints = optionText.pointsOsteoporosis
                    val osteoarthritisPoints = optionText.pointsArthritis
                    val answerText = optionText.text ?: "Invalid Question"
                    onOptionSelected(
                        questionItem.question,
                        answerText,
                        osteoporosisPoints,
                        osteoarthritisPoints,
                        ""
                    )
                }
            }

            holder.binding.answer.addView(radioButton)
        }} else {
            holder.binding.etAnswer.visibility = View.VISIBLE
            holder.binding.answer.visibility = View.GONE

            holder.binding.etAnswer.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    onOptionSelected(questionItem.question, "", 0, 0, s.toString())
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

        }

        val anim = AlphaAnimation(0.3f, 1.0f).apply {
            duration = 300
            repeatMode = Animation.REVERSE
            repeatCount = 2
        }
        holder.itemView.startAnimation(anim)


    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DemographicTestResponseItem>() {
            override fun areItemsTheSame(oldItem: DemographicTestResponseItem, newItem: DemographicTestResponseItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DemographicTestResponseItem, newItem: DemographicTestResponseItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}