package com.teguh.storyapp

import com.teguh.storyapp.data.local.entity.StoryEntity
import com.teguh.storyapp.data.remote.request.RequestLogin
import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.*

object DataDummy {
    fun generateDummyStoryEntity(): ResponseGetStory {
        val storyList : MutableList<StoryEntity> = arrayListOf()
        for (i in 0..10) {
            val stories = StoryEntity(
                "story-$i",
                "dicoding",
                "dicoding story desc",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-10-03T03:48:09.330Z",
                -6.288288288288288,
                106.82289451908555,
            )
            storyList.add(stories)
        }

        return ResponseGetStory(
            false,
            "Stories fetched successfully",
            storyList,
        )
    }

    fun generateDummyStories(): List<StoryEntity> {
        val items: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                "story-$i",
                "dicoding",
                "dicoding story desc",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-10-03T03:48:09.330Z",
                -6.288288288288288,
                106.82289451908555,
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyGetStoryResponse(): ResponseGetStory {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<StoryEntity>()

        for (i in 0 until 10) {
            val story = StoryEntity(
                "story-$i",
                "dicoding",
                "dicoding story desc",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "2022-10-03T03:48:09.330Z",
                -12.321,
                -12.232
            )
            listStory.add(story)
        }
        return ResponseGetStory(error, message, listStory)
    }

    fun generateDummyRegisterResponseError(): ResponseRegister {
        return ResponseRegister(
            true,
            "Email is already taken"
        )
    }

    fun generateDummyRegisterResponse(): ResponseRegister {
        return ResponseRegister(
            false,
            "User Created"
        )
    }

    fun generateDummyAddStoryResponse(): ResponseAddStory {
        return ResponseAddStory(
            false,
            "success"
        )
    }

    fun generateDummyRegisterRequestBody(): RequestRegister {
        return RequestRegister(
            "testing",
            "testing12@gmail.com",
            "testing12345"
        )
    }

    fun generateDummyLoginRequest(): RequestLogin {
        return RequestLogin(
            "tghstwn@gmail.com",
            "tghswtn160",
        )
    }

    fun generateDummyLoginResponse(): ResponseLogin {
        val loginResult = LoginResult(
            "user-testing",
            "testing",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        )

        return ResponseLogin(
            false,
            "success",
            loginResult
        )
    }

    fun generatedDummyToken(): String {
        return "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLUhuTDBhdnZaeDVwNEdFV2UiLCJpYXQiOjE2NjI2OTU0MzJ9.MzLbkXmkRkPkAwxw7ZmrAOp59TDlp4DtffKGDi31pT8"
    }

}