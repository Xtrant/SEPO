package com.example.sepo.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sepo.data.Repository
import com.example.sepo.data.response.DoctorResponseItem
import com.example.sepo.data.response.ListUserResponseItem
import com.example.sepo.result.Result
import kotlinx.coroutines.launch

class ListUserViewModel(private val repository: Repository): ViewModel() {
    private val _resultListUser = MutableLiveData<Result<List<ListUserResponseItem>>>()
    val resultListUser : LiveData<Result<List<ListUserResponseItem>>> = _resultListUser

    private val _resultListDoctor = MutableLiveData<Result<List<DoctorResponseItem>>>()
    val resultListDoctor : LiveData<Result<List<DoctorResponseItem>>> = _resultListDoctor

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
}