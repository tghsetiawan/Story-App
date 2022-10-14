package com.teguh.storyapp.data.remote.retrofit

import com.teguh.storyapp.data.remote.response.ResponseAddStory
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {
    //function add new story
    @Multipart
    @POST("stories")
    suspend fun addStory (
        @Header("Authorization") token: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") lon: RequestBody?,
    ): ResponseAddStory

    //function get new story
    @GET("stories")
    suspend fun getStory(
        @Header("Authorization") token: String?,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int,
    ): ResponseGetStory
}