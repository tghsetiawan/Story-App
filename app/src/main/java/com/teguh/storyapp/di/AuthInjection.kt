package com.teguh.storyapp.di

import com.teguh.storyapp.data.remote.retrofit.ApiConfig
import com.teguh.storyapp.data.repository.UserAuthRepository

object AuthInjection {
    fun provideRepository(): UserAuthRepository {
        val apiService = ApiConfig.getApiServiceAuth()
        return UserAuthRepository.getInstance(apiService)
    }
}