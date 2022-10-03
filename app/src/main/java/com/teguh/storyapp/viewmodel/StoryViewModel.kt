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
    fun addStory(token: String?, file: MultipartBody.Part?, description: RequestBody?, lat: RequestBody?, lon: RequestBody?) = storyAppRepository.addStory(token, file, description, lat, lon)

    fun getMapStories(token: String?, page: Int?, size: Int?, location: Int?) = storyAppRepository.getMapStory(token, page, size, location)

    fun getStories(token: String) : LiveData<PagingData<StoryEntity>> = storyAppRepository.getStory(token).cachedIn(viewModelScope)
}