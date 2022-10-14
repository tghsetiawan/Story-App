package com.teguh.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.repository.StoryAppRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryViewModel (private val storyAppRepository: StoryAppRepository) : ViewModel(){
    fun addStory(file: MultipartBody.Part?, description: RequestBody?, lat: RequestBody?, lon: RequestBody?) = storyAppRepository.addStory(file, description, lat, lon)

    fun getMapStories(page: Int?, size: Int?, location: Int?) = storyAppRepository.getMapStory(page, size, location)

    fun getStories(): LiveData<PagingData<StoryEntity>> = storyAppRepository.getStory().cachedIn(viewModelScope)
}