package com.teguh.storyapp.di

import android.content.Context
import com.teguh.storyapp.data.repository.StoryAppRepository
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.remote.retrofit.ApiConfig
import com.teguh.storyapp.utils.Constant
import com.teguh.storyapp.utils.getPreference

object StoryInjection {
    fun provideRepository(context: Context): StoryAppRepository {
        val token = getPreference(context, Constant.USER_TOKEN)
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryAppRepository(token, apiService, database)
    }
}