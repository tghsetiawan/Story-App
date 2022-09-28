package com.teguh.storyapp.data.remote.retrofit

import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.ResponseAddStory
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import com.teguh.storyapp.data.remote.response.ResponseLogin
import com.teguh.storyapp.data.remote.response.ResponseRegister
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {
    @Headers("Content-Type: application/json")

    //function sign up or register
    @POST("register")
    suspend fun register(@Body request: RequestRegister): ResponseRegister

    //function sign in or login
    @POST("login")
    suspend fun login(@Body request: RequestLogin): ResponseLogin

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
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int = 0,
    ): ResponseGetStory
}