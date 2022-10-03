package com.teguh.storyapp

import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.ResponseRegister

object DataDummy {
    fun generateDummyStoryEntity(): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        for (i in 0..10) {
            val stories = StoryEntity(
                "story-$i",
                "dicoding",
                "dicoding story desc",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-10-03T03:48:09.330Z",
                null,
                null,
            )
            storyList.add(stories)
        }
        return storyList
    }

    fun generateDummyRegisterResponse(): ResponseRegister {
        val res = ResponseRegister(
            false,
            "User Created"
        )
        return res
    }

    fun generateDummyRegisterRequestBody(): RequestRegister {
        val req = RequestRegister(
            "testing",
            "testing@gmail.com",
            "testing12345"
        )
        return  req
    }
}