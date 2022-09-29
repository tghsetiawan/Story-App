package com.teguh.storyapp.di

import android.content.Context
import com.teguh.storyapp.data.remote.retrofit.ApiConfig
import com.teguh.storyapp.data.repository.UserAuthRepository

object AuthInjection {
    fun provideRepository(): UserAuthRepository {
        val apiService = ApiConfig.getApiService()
        return UserAuthRepository.getInstance(apiService)
    }
}