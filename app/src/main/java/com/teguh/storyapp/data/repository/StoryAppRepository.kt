package com.teguh.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.teguh.storyapp.data.Result
import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.local.room.StoryDatabase
import com.teguh.storyapp.data.paging.StoryRemoteMediator
import com.teguh.storyapp.data.remote.response.ResponseAddStory
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import com.teguh.storyapp.data.remote.retrofit.ApiService
import com.teguh.storyapp.utils.ErrorUtils.getErrorThrowableMsg
import com.teguh.storyapp.utils.Param.Companion.TAG
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.lang.Exception

class StoryAppRepository(
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {
    fun addStory(token: String?, file: MultipartBody.Part?, description: RequestBody?, lat: RequestBody?, lon: RequestBody?): LiveData<Result<ResponseAddStory>> = liveData {
        emit(Result.Loading)
        try{
            val response = apiService.addStory(token, file!!, description!!, lat, lon)

            Log.e(TAG, "Response Add Story : $response")

            if (response.error == false){
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message.toString()))
            }

        } catch (e: Exception){
            Log.e(TAG, "Exception Add Story : ${e.message.toString()}" )
            emit(Result.Error(getErrorThrowableMsg(e)))
        }
    }

    fun getStory(token: String, page: Int?, size: Int?, location: Int?): LiveData<Result<ResponseGetStory>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getStory(token, page!!, size!!, location!!)

            Log.e(TAG, "Response Get Story : $response" )

            if (response.error == false){
                emit(Result.Success(response))
            } else {
                emit(Result.Error(response.message.toString()))
            }

        } catch (e: Exception) {
            Log.e(TAG, "Exception Get Story : ${e.message.toString()}")
            emit(Result.Error(getErrorThrowableMsg(e)))
        }
    }

    fun getMapStory(token: String): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}