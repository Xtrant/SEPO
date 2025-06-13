package com.example.sepo.ui.bottomnav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.databinding.FragmentExerciseBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.RecommendAdapter
import com.example.sepo.ui.education.EducationActivity
import com.example.sepo.ui.exercise.DetailExerciseActivity
import com.example.sepo.ui.list.ListUserViewModel
import com.example.sepo.utils.showLoading


class ExerciseFragment : Fragment() {
    private val viewModel: ListUserViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }

    private var _binding: FragmentExerciseBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExerciseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        viewModel.listExercise()
    }

    private fun observeViewModel() {
        viewModel.resultListExercise.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding?.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding?.progressBar)
                    val adapter = RecommendAdapter(result.data, requireContext()) { video ->

                        startActivity(Intent(requireContext(), DetailExerciseActivity::class.java))


                    }
                    binding?.rvExerciseOsteoporosis?.layoutManager = LinearLayoutManager(context)
                    binding?.rvExerciseOsteoporosis?.adapter = adapter
                }


                is Result.Error -> {
                    showLoading(false, binding?.progressBar)

                }
            }
        }
    }

}