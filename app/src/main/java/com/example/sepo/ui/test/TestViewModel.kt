package com.example.sepo.ui.test

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sepo.data.Repository
import com.example.sepo.data.response.PostTestResponse
import com.example.sepo.data.response.PostTestResponseItem
import com.example.sepo.data.response.PreTestResponse
import com.example.sepo.data.response.PreTestResponseItem
import com.example.sepo.result.Result
import kotlinx.coroutines.launch

class TestViewModel(private val repository: Repository) : ViewModel() {
    private val _preTestResult = MutableLiveData<Result<List<PreTestResponseItem>>>()
    val preTestResult: LiveData<Result<List<PreTestResponseItem>>> = _preTestResult

    private val _postTestResult = MutableLiveData<Result<List<PostTestResponseItem>>>()
    val postTestResult: LiveData<Result<List<PostTestResponseItem>>> = _postTestResult


    fun getPreTest() {
        viewModelScope.launch {
            _preTestResult.value = Result.Loading
            _preTestResult.value = repository.getPreTest()
        }
    }

    fun getPostTest() {
        viewModelScope.launch {
            _postTestResult.value = Result.Loading
            _postTestResult.value = repository.getPostTest()
        }
    }

}