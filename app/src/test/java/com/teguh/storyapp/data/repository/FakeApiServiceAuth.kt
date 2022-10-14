package com.teguh.storyapp.data.repository

import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.ResponseLogin
import com.teguh.storyapp.data.remote.response.ResponseRegister
import com.teguh.storyapp.DataDummy
import com.teguh.storyapp.data.remote.retrofit.ApiServiceAuth

class FakeApiServiceAuth : ApiServiceAuth {
    private val dummyRegisterResponse = DataDummy.generateDummyRegisterResponse()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    override suspend fun register(request: RequestRegister): ResponseRegister {
        return dummyRegisterResponse
    }

    override suspend fun login(request: RequestLogin): ResponseLogin {
        return dummyLoginResponse
    }
}