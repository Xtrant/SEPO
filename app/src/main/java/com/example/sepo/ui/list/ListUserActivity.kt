package com.example.sepo.ui.list

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sepo.R
import com.example.sepo.data.response.ListUserResponseItem
import com.example.sepo.databinding.ActivityListBinding
import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.result.Result
import com.example.sepo.utils.showLoading
import com.example.sepo.utils.showToast

class ListUserActivity : AppCompatActivity() {
    private lateinit var binding : ActivityListBinding

    private val viewModel by viewModels<ListUserViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.listUser()

        observeViewModel()

        setupRecyclerView()
    }

    private fun observeViewModel() {
        viewModel.resultListUser.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true, binding.progressBar)

                is Result.Success -> {
                    showLoading(false, binding.progressBar)
                    val listUser = result.data
                    setListUser(listUser)


                }

                is Result.Error -> {
                    showLoading(false, binding.progressBar)
                    showToast(this, result.error)
                }
            }
        }
    }

    private fun setListUser(user: List<ListUserResponseItem?>?) {
        val adapter = ListUserAdapter()
        adapter.submitList(user)
        binding.rvList.adapter = adapter
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvList.layoutManager = layoutManager
    }
}