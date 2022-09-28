package com.teguh.storyapp.di

import android.content.Context
import com.teguh.storyapp.data.remote.retrofit.ApiConfig
import com.teguh.storyapp.data.repository.UserAuthRepository

object AuthInjection {
    fun provideRepository(context: Context): UserAuthRepository {
        val apiService = ApiConfig.getApiService()
        return UserAuthRepository.getInstance(apiService)
    }
}