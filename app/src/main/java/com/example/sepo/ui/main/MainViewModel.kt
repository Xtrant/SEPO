package com.example.sepo.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sepo.data.Repository
import com.example.sepo.data.response.ConnectionResponse
import com.example.sepo.result.Result

class MainViewModel(private val repository: Repository) : ViewModel() {

    private val _saveUserResult = MutableLiveData<Result<ConnectionResponse>>()
    val saveUserResult: LiveData<Result<ConnectionResponse>> = _saveUserResult

}