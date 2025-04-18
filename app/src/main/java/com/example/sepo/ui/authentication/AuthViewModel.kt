package com.example.sepo.ui.authentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sepo.data.Repository
import com.example.sepo.data.response.ConnectionResponse
import com.example.sepo.result.Result
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository) : ViewModel() {

    private val _saveUserResult = MutableLiveData<Result<ConnectionResponse>>()
    val saveUserResult: LiveData<Result<ConnectionResponse>> = _saveUserResult

    fun saveUser(uid: String, name: String, email: String, photoUrl: String) {
        _saveUserResult.value = Result.Loading

        viewModelScope.launch {
            val result = repository.saveUser(uid, name, email, photoUrl)
            _saveUserResult.value = result
        }
    }
}