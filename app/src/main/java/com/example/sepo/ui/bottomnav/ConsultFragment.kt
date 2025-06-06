package com.example.sepo.ui.bottomnav

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.databinding.FragmentConsultBinding
import com.example.sepo.result.Result
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.ui.adapter.DoctorAdapter
import com.example.sepo.ui.consult.LiveChatActivity
import com.example.sepo.ui.list.ListUserViewModel
import com.example.sepo.utils.showLoading

class ConsultFragment : Fragment() {
    private var _binding: FragmentConsultBinding? = null
    private val binding get() = _binding
    private val viewModel: ListUserViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentConsultBinding.inflate(inflater, container,false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        viewModel.listDoctor()
    }

    private fun observeViewModel() {
        viewModel.resultListDoctor.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true, binding?.progressBar)
                }

                is Result.Success -> {
                    showLoading(false, binding?.progressBar)
                    val adapter = DoctorAdapter(result.data) {doctor ->
                        val intent = Intent(requireContext(), LiveChatActivity::class.java)
                        intent.putExtra("id_doctor", doctor.id)
                        intent.putExtra("name_doctor", doctor.name)
                        startActivity(intent)
                    }
                    binding?.recyclerView?.layoutManager = LinearLayoutManager(context)
                    binding?.recyclerView?.adapter = adapter
                }


                is Result.Error -> {
                    showLoading(false, binding?.progressBar)

                }
            }
        }
    }

}