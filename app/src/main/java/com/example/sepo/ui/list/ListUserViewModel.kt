package com.example.sepo.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sepo.data.Repository
import com.example.sepo.data.response.DoctorResponseItem
import com.example.sepo.data.response.ListUserResponseItem
import com.example.sepo.data.response.VideoResponseItem
import com.example.sepo.result.Result
import kotlinx.coroutines.launch

class ListUserViewModel(private val repository: Repository): ViewModel() {
    private val _resultListUser = MutableLiveData<Result<List<ListUserResponseItem>>>()
    val resultListUser : LiveData<Result<List<ListUserResponseItem>>> = _resultListUser

    private val _resultListDoctor = MutableLiveData<Result<List<DoctorResponseItem>>>()
    val resultListDoctor : LiveData<Result<List<DoctorResponseItem>>> = _resultListDoctor

    private val _resultListEdukasi = MutableLiveData<Result<List<VideoResponseItem>>>()
    val resultListEdukasi : LiveData<Result<List<VideoResponseItem>>> = _resultListEdukasi

    private val _resultListExercise = MutableLiveData<Result<List<VideoResponseItem>>>()
    val resultListExercise : LiveData<Result<List<VideoResponseItem>>> = _resultListExercise

    private val _webViewVisibility = MutableLiveData<MutableMap<Int, Boolean>>()
    val webViewVisibility: LiveData<MutableMap<Int, Boolean>> = _webViewVisibility



    fun toggleVisibility(position: Int) {
        val current = _webViewVisibility.value ?: mutableMapOf()
        current[position] = current[position] != true
        _webViewVisibility.value = current
    }

    fun getVisibility(position: Int): Boolean {
        return _webViewVisibility.value?.get(position) ?: false
    }

    fun listUser() {
        viewModelScope.launch {
            _resultListUser.value = Result.Loading
            _resultListUser.value = repository.listUser()
        }
    }

    fun listDoctor() {
        viewModelScope.launch {
            _resultListDoctor.value = Result.Loading
            _resultListDoctor.value = repository.getDoctor()
        }
    }

    fun listEdukasi() {
        viewModelScope.launch {
            _resultListEdukasi.value = Result.Loading
            _resultListEdukasi.value = repository.getEdukasi()
        }
    }

    fun listExercise() {
        viewModelScope.launch {
            _resultListExercise.value = Result.Loading
            _resultListExercise.value = repository.getExercise()
        }
    }
}