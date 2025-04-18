package com.example.sepo.di

import com.example.sepo.ui.ViewModelFactory
import com.example.sepo.data.Repository
import com.example.sepo.data.retrofit.ApiConfig

object Injection {
    fun provideViewModelFactory(): ViewModelFactory {
        val apiService = ApiConfig.getApiService()
        val repository = Repository.getInstance(apiService)

        return ViewModelFactory(repository)
    }
}