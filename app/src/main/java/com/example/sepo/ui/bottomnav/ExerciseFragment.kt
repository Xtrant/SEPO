package com.example.sepo.ui.bottomnav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sepo.R
import com.example.sepo.databinding.FragmentEducationBinding


class ExerciseFragment : Fragment() {
    private var _binding: FragmentEducationBinding? = null
    private val binding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentEducationBinding.inflate(inflater, container,false)
        return binding?.root
    }

}