package com.teguh.storyapp.di

import android.content.Context
import com.teguh.storyapp.data.repository.StoryAppRepository
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.remote.retrofit.ApiConfig

object StoryInjection {
    fun provideRepository(context: Context): StoryAppRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(apiService, database)
    }
}