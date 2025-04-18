package com.example.sepo.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sepo.data.Repository
import com.example.sepo.di.Injection
import com.example.sepo.ui.authentication.AuthViewModel
import com.example.sepo.ui.list.ListUserViewModel
import com.example.sepo.ui.main.MainViewModel
import com.example.sepo.ui.profile.ProfileViewModel
import com.example.sepo.ui.test.TestViewModel

class ViewModelFactory(
    private val repository: Repository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            MainViewModel::class.java -> MainViewModel(repository) as T

            ListUserViewModel::class.java -> ListUserViewModel(repository) as T

            ProfileViewModel::class.java -> ProfileViewModel(repository) as T

            AuthViewModel::class.java -> AuthViewModel(repository) as T

            TestViewModel::class.java -> TestViewModel(repository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(mApplication: Application): ViewModelFactory =
            INSTANCE ?: synchronized(this) {
                Injection.provideViewModelFactory()
            }.also { INSTANCE = it }
    }
}