package com.example.sepo.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sepo.data.Repository
import com.example.sepo.data.response.ConnectionResponse
import com.example.sepo.data.response.ProfileResponseItem
import com.example.sepo.result.Result
import kotlinx.coroutines.launch

class ProfileViewModel(private val repo: Repository) : ViewModel() {
    private val _profileResult = MutableLiveData<Result<List<ProfileResponseItem>>>()
    val profileResult: LiveData<Result<List<ProfileResponseItem>>> = _profileResult

    private val _createResult = MutableLiveData<Result<ConnectionResponse>>()
    val createResult: LiveData<Result<ConnectionResponse>> = _createResult

    fun createProfile(uid: String, name: String, age: Int, gender: String) {
        viewModelScope.launch {
            _createResult.value = Result.Loading
            _createResult.value = repo.createProfile(uid, name, age, gender)
        }
    }

    fun getProfiles(uid: String) {
        viewModelScope.launch {
            _profileResult.value = Result.Loading
            _profileResult.value = repo.getProfiles(uid)
        }
    }
}
