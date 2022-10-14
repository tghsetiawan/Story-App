package com.teguh.storyapp.data.repository

import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.data.remote.response.ResponseAddStory
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import com.teguh.storyapp.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyAddStoryResponse = DataDummy.generateDummyAddStoryResponse()
    private val dummyGetStoryResponse = DataDummy.generateDummyGetStoryResponse()

    override suspend fun addStory(
        token: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): ResponseAddStory {
        return dummyAddStoryResponse
    }

    override suspend fun getStory(
        token: String?,
        page: Int,
        size: Int,
        location: Int
    ): ResponseGetStory {
        return dummyGetStoryResponse
    }
}