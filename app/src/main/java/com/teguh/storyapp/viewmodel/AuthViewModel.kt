package com.teguh.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.repository.UserAuthRepository

class AuthViewModel (private val userAuthRepository: UserAuthRepository) : ViewModel(){
    fun register(requestRegister: RequestRegister) = userAuthRepository.register(requestRegister)
    fun login(requestLogin: RequestLogin) = userAuthRepository.login(requestLogin)
}