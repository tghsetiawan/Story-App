package com.teguh.storyapp

import com.teguh.storyapp.data.remote.request.RequestRegister
import com.teguh.storyapp.data.remote.response.ListStory
import com.teguh.storyapp.data.remote.response.ResponseGetStory
import com.teguh.storyapp.data.remote.response.ResponseRegister

object DataDummy {
    fun generateDummyStoryEntity(): ResponseGetStory {
        val storyList = ArrayList<ListStory>()
        for (i in 0..10) {
            val stories = ListStory(
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

        val stories = ResponseGetStory(
            false,
            "Stories fetched successfully",
            storyList,
        )
        return stories
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