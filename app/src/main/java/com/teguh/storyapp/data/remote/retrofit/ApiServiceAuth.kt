package com.teguh.storyapp.data.remote.retrofit

import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.ResponseLogin
import com.teguh.storyapp.data.remote.response.ResponseRegister
import retrofit2.http.*

interface ApiServiceAuth {
    @Headers("Content-Type: application/json")

    //function sign up or register
    @POST("register")
    suspend fun register(@Body request: RequestRegister): ResponseRegister

    //function sign in or login
    @POST("login")
    suspend fun login(@Body request: RequestLogin): ResponseLogin
}