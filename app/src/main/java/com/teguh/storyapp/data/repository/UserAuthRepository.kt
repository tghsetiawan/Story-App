package com.teguh.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.ResponseLogin
import com.teguh.storyapp.data.remote.response.ResponseRegister
import com.teguh.storyapp.data.remote.retrofit.ApiServiceAuth
import com.teguh.storyapp.utils.ErrorUtils
import com.teguh.storyapp.utils.Param
import java.lang.Exception

class UserAuthRepository(
private val apiService: ApiServiceAuth
) {
    fun register(request: RequestRegister): LiveData<Result<ResponseRegister>> = liveData {
        emit(Result.Loading)
        try {
            Log.e(Param.TAG, "Request Register : $request")

            val response = apiService.register(request)

            Log.e(Param.TAG, "Response Register : $response")

            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message.toString()))
            }

        } catch (e: Exception) {
            Log.e(Param.TAG, "Exception Register : ${e.message.toString()}")
            emit(Result.Error(ErrorUtils.getErrorThrowableMsg(e)))
        }
    }

    fun login(request: RequestLogin): LiveData<Result<ResponseLogin>> = liveData {
        emit(Result.Loading)
        try {
            Log.e(Param.TAG, "Request Login : $request")

            val response = apiService.login(request)

            Log.e(Param.TAG, "Response Login : $response")

            if (response.error == false) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message.toString()))
            }

        } catch (e: Exception) {
            Log.e(Param.TAG, "Exception Login : ${e.message.toString()}")
            emit(Result.Error(ErrorUtils.getErrorThrowableMsg(e)))
        }
    }

    companion object {
        @Volatile
        private var instance: UserAuthRepository? = null
        fun getInstance(
            apiService: ApiServiceAuth
        ): UserAuthRepository =
            instance ?: synchronized(this) {
                instance ?: UserAuthRepository(apiService)
            }.also { instance = it }
    }
}